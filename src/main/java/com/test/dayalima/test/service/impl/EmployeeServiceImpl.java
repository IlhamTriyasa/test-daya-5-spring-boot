package com.test.dayalima.test.service.impl;

import com.test.dayalima.test.production.model.Employee;
import com.test.dayalima.test.production.repo.EmployeeProdRepo;
import com.test.dayalima.test.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeProdRepo employeeProdRepo;

    @Override
    public Page<Employee> getPagingAllData(String searchParam, Pageable pageable) {
        if (searchParam != "" && searchParam != null){
            return employeeProdRepo.findByIdOrEmailLikeOrNameLike(searchParam,searchParam, searchParam,pageable);
        }else{
            return employeeProdRepo.findAll(pageable);
        }
    }

    @Override
    public Employee findById(String Id) {
        return employeeProdRepo.findById(Id).get();
    }
}
