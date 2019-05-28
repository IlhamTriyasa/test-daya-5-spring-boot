package com.test.dayalima.test.controller.api;

import com.test.dayalima.test.exception.AppException;
import com.test.dayalima.test.model.Role;
import com.test.dayalima.test.model.RoleName;
import com.test.dayalima.test.model.User;
import com.test.dayalima.test.payloads.ApiResponse;
import com.test.dayalima.test.payloads.JwtAuthenticationResponse;
import com.test.dayalima.test.payloads.LoginRequest;
import com.test.dayalima.test.payloads.SignUpRequest;
import com.test.dayalima.test.production.repo.RoleProdRepo;
import com.test.dayalima.test.production.repo.UserProdRepo;
import com.test.dayalima.test.repo.RoleRepo;
import com.test.dayalima.test.repo.UserRepo;
import com.test.dayalima.test.security.JwtTokenProvider;
import com.test.dayalima.test.service.LogApiHitService;
import com.test.dayalima.test.util.LocalIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepo userRepository;

    @Autowired
    RoleRepo roleRepository;

    @Autowired
    UserProdRepo userProdRepo;

    @Autowired
    RoleProdRepo roleProdRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    LogApiHitService logApiHitService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.OK.toString(),"Success");
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.BAD_REQUEST.toString(),"Username is already taken!");
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.BAD_REQUEST.toString(),"Email Address already in use!");
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
                signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        // Creating user's account on production
        com.test.dayalima.test.production.model.User userProd = new com.test.dayalima.test.production.model.User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
                signUpRequest.getPassword());
        userProd.setPassword(passwordEncoder.encode(user.getPassword()));

        com.test.dayalima.test.production.model.Role userRoleProd = roleProdRepo.findByName(com.test.dayalima.test.production.model.RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        userProd.setRoles(Collections.singleton(userRoleProd));

        com.test.dayalima.test.production.model.User resultProd = userProdRepo.save(userProd);


        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.OK.toString(),"User registered successfully");
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
