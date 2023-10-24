package com.fernandoroa.testingApp.controller;

import com.fernandoroa.testingApp.model.entities.Employee;
import com.fernandoroa.testingApp.service.implementations.EmployeeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Employee saveEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    public List<Employee> listEmployees() {
        return employeeService.getAllEmployee();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long employeeId) {
        return employeeService.getEmployeeById(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
        @PathVariable("id") Long employeeId,
        @RequestBody Employee employee
    ){
        return this.employeeService.getEmployeeById(employeeId)
                .map( savedEmployee -> {
                    savedEmployee.setName(employee.getName());
                    savedEmployee.setLastname(employee.getLastname());
                    savedEmployee.setEmail(employee.getEmail());

                    Employee updatedEmployee = this.employeeService.saveEmployee(savedEmployee);
                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
                })
                .orElseGet( () -> ResponseEntity.notFound().build() );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId ) {
        this.employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>("Empleado eliminado", HttpStatus.OK);
    }
}
