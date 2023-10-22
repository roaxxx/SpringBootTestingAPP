package com.fernandoroa.testingApp.service.implementations;

import com.fernandoroa.testingApp.exception.ResourceNotFoundException;
import com.fernandoroa.testingApp.model.entities.Employee;
import com.fernandoroa.testingApp.repositories.EmployeeRepository;
import com.fernandoroa.testingApp.service.IEmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository
                .findByEmail(employee.getEmail());

        if(savedEmployee.isPresent()) {
            throw new ResourceNotFoundException(
                    "El empleado con este email, ya existe: "+employee.getEmail()
                );
        }
        return  employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}
