package com.test.dayalima.test.controller.api;

import com.test.dayalima.test.dto.LogApiHitSummary;
import com.test.dayalima.test.payloads.ApiResponse;
import com.test.dayalima.test.production.dto.EmployeeRequest;
import com.test.dayalima.test.production.model.Employee;
import com.test.dayalima.test.production.repo.EmployeeProdRepo;
import com.test.dayalima.test.repo.LogApiHitRepo;
import com.test.dayalima.test.service.EmployeeService;
import com.test.dayalima.test.service.LogApiHitService;
import com.test.dayalima.test.util.LocalIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    EmployeeProdRepo employeeProdRepo;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    LogApiHitService logApiHitService;

    @Autowired
    LogApiHitRepo logApiHitRepo;


    @GetMapping("/employee")
    @PreAuthorize("hasRole('USER')")
    public Page<Employee> index(Pageable pageRequest, @RequestParam(name = "searchParam", required = false) String searchParam, HttpServletRequest request){
        String srcParam = searchParam;
        if (searchParam != "" && searchParam != null){
            srcParam = "%"+searchParam+"%";
        }
        try {
            Page<Employee> employeePage = employeeService.getPagingAllData(srcParam, pageRequest);
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.OK.toString(),"Success");
            return employeePage;
        }catch (Exception ex){
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage());
            return null;
        }
    }

    @GetMapping("/employee/{id}")
    @PreAuthorize("hasRole('USER')")
    public Employee findById(@PathVariable(name = "id") String Id, HttpServletRequest request){
        try {
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.OK.toString(),"Success");
            return employeeService.findById(Id);
        }catch (Exception ex){
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage());
            return null;
        }
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getSummary(HttpServletRequest request){
        try{
            List<LogApiHitSummary> logApiHitSummaryList = logApiHitService.getSummary();
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.OK.toString(),"Success");
            return new ResponseEntity(logApiHitSummaryList, HttpStatus.OK);
        }catch (Exception ex){
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),ex.getMessage());
            return new ResponseEntity(new ApiResponse(false, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/employee")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeRequest employeeRequest, HttpServletRequest request){
        if (employeeProdRepo.existsByEmail(employeeRequest.getEmail())){
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.BAD_REQUEST.toString(),"Email is already taken!");
            return new ResponseEntity(new ApiResponse(false, "Email is already taken!"), HttpStatus.BAD_REQUEST);
        }

        Employee employee = new Employee();
        employee.setId(employeeRequest.getId());
        employee.setEmail(employeeRequest.getEmail());
        employee.setName(employeeRequest.getName());
        try {
            employeeProdRepo.save(employee);
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.OK.toString(),"Employee has been added!");
            return new ResponseEntity(new ApiResponse(true, "Employee has been added!"), HttpStatus.OK);
        }catch (Exception ex){
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage());
            return new ResponseEntity(new ApiResponse(false, ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/employee")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateEmployee(@Valid @RequestBody EmployeeRequest employeeRequest, HttpServletRequest request){

        Employee employee = employeeProdRepo.findById(employeeRequest.getId()).get();
        employee.setEmail(employeeRequest.getEmail());
        employee.setName(employeeRequest.getName());

        try {
            employeeProdRepo.save(employee);
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.OK.toString(),"Employee has been added!");
            return new ResponseEntity(new ApiResponse(true, "Employee has been updated!"), HttpStatus.OK);
        }
        catch (Exception ex){
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage());
            return new ResponseEntity(new ApiResponse(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/employee/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request){
        try {
            employeeProdRepo.deleteById(id);
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.OK.toString(),"Employee has been added!");
            return new ResponseEntity(new ApiResponse(true,"Employee has been deleted!"), HttpStatus.OK);
        }catch (Exception ex){
            logApiHitService.AddLogApiHit(LocalIP.LocalIPAddress(),request.getRequestURL().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage());
            return new ResponseEntity(new ApiResponse(false,"Delete employee failed!"), HttpStatus.BAD_REQUEST);
        }
    }
}
