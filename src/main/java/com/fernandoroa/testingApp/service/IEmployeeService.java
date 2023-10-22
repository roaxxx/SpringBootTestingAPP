package com.fernandoroa.testingApp.service;

import com.fernandoroa.testingApp.model.entities.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployee();

    Optional<Employee> getEmployeeById(Long employeeId);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Long employeeId);
}
