package com.test.dayalima.test.production.repo;

import com.test.dayalima.test.production.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProdRepo extends JpaRepository<Employee, String> {
    @Query(
            value = "select em from Employee em where em.id = :param or em.email like %:param% or em.name like %:param%"
    )
    Page<Employee> getPagingAllDataWithParam(@Param("param")String searchParam, Pageable pageable);

    Page<Employee> findByIdOrEmailLikeOrNameLike(String Id,String Email, String Name, Pageable pageable);

    Boolean existsByEmail(String email);
}
