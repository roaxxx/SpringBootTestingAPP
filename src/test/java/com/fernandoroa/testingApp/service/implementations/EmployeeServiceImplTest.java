package com.fernandoroa.testingApp.service.implementations;

import com.fernandoroa.testingApp.exception.ResourceNotFoundException;
import com.fernandoroa.testingApp.model.entities.Employee;
import com.fernandoroa.testingApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    @BeforeEach
    void setup(){
        employee = Employee.builder()
                .name("Jose")
                .lastname("Vela")
                .email("jose@gmail.com")
                .build();
    }

    @DisplayName("Test to save an Employee")
    @Test
    void saveEmployee() {
        //Given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //When
        Employee employee1 = employeeService.saveEmployee(employee);

        //That
        assertThat(employee1).isNotNull();

    }

    @DisplayName("Test to save an Employee with ThrowException")
    @Test
    void saveEmployeeWithThrowException(){
        //Given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //When
        assertThrows(ResourceNotFoundException.class,()->{
           employeeService.saveEmployee(employee);
        });

        //That
        verify(employeeRepository,never()).save(any(Employee.class));
    }

    @DisplayName("Test to list Employees on DB")
    @Test
    void getAllEmployee() {
        //given
        Employee employee1 = Employee.builder()
                .id(1L)
                .name("Alejandro")
                .lastname("Caballero")
                .email("alecaballero@gmail.com")
                .build();

        //When
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        List<Employee> employees = employeeService.getAllEmployee();

        //Then
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    @DisplayName("Test to return empty list ")
    @Test
    void getEmptyEmployeesList() {
        //given
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());


        //When
        List<Employee> employees = employeeService.getAllEmployee();

        //Then
        assertThat(employees).isEqualTo(Collections.emptyList());
        assertThat(employees.size()).isEqualTo(0);
    }

    @DisplayName("Test to get an Employee by Id")
    @Test
    void getEmployeeById() {
        //Given
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        //When
        Employee employeeFound = employeeService.getEmployeeById(employee.getId()).get();

        //That
        assertThat(employeeFound).isNotNull();
    }

    @DisplayName("Test to update an Employee")
    @Test
    void updateEmployee() {
        //Given
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setName("Arnold");
        employee.setLastname("Gevara");
        //When
        Employee updatedEmploye = employeeService.updateEmployee(employee);
        //Then
        assertThat(updatedEmploye.getName()).isEqualTo("Arnold");
        assertThat(updatedEmploye.getLastname()).isEqualTo("Gevara");
    }
    @DisplayName("Test to delete an Employee")
    @Test
    void deleteEmployee() {
        //Given
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //When
        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}