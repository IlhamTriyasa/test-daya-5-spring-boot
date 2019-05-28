package com.test.dayalima.test.production.repo;

import com.test.dayalima.test.production.model.Role;
import com.test.dayalima.test.production.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleProdRepo extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);
}
