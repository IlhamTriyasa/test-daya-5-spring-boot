package com.test.dayalima.test.service;

import com.test.dayalima.test.production.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    Page<Employee> getPagingAllData(String searchParam, Pageable pageable);

    Employee findById(String Id);
}
