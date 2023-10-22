package com.fernandoroa.testingApp.repositories;

import com.fernandoroa.testingApp.model.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    @BeforeEach
    void setup(){
        employee = Employee.builder()
                .name("Jose")
                .lastname("Vela")
                .email("jose@gmail.com")
                .build();
    }

    @DisplayName("Test to save an employee")
    @Test
    void saveEmployeeTest() {
        //given  - Se da una condición previa o configuración.

        Employee employee1 = Employee.builder()
                .name("William")
                .lastname("roa")
                .email("arframg@gmail.com")
                .build();

        //When - Acción o el comportamiento que se va a probar

        Employee savedEmployee = employeeRepository.save(employee1);

        //Then - Verificar cual ha sido el resultado de la prueba
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Test to display employees")
    @Test
    public void listAllEmployees(){
        //Given
        Employee employee1 = Employee.builder()
                .name("Fernando")
                .lastname("vargas")
                .email("arfra2mg@gmail.com")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee);

        //When
        List<Employee> employeeList = employeeRepository.findAll();

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("Test to delete an employee")
    @Test
    void testDeleteEmployee(){
        //Given
        employeeRepository.save(employee);

        //When
        employeeRepository.deleteById(employee.getId());

        Optional<Employee> employee1 = employeeRepository.findById(employee.getId());

        assertThat(employee1).isEmpty();
    }

    @DisplayName("Test to get an employee by Id")
    @Test
    void testToGetEmployeeById(){
        // Given
        Employee employeeDB = employeeRepository.save(employee);

        //When
        Optional<Employee> employee1 = employeeRepository.findById(employeeDB.getId());

        //Then
        assertThat(employee1.isPresent()).isEqualTo(true);
        assertThat(employee1.get().getEmail()).isEqualTo("jose@gmail.com");
    }

    @DisplayName("Test to update and employee")
    @Test
    void testUpdateEmployee() {
        //Given
        employeeRepository.save(employee);

        Employee employee1 = employeeRepository.findById(employee.getId()).get();

        employee1.setEmail("rwillian@jdc.edu.co");
        employee1.setName("William Fernando");

        Employee updatedEmployee = employeeRepository.save(employee1);

        //Then
        assertThat(updatedEmployee.getEmail()).isEqualTo("rwillian@jdc.edu.co");
        assertThat(updatedEmployee.getName()).isEqualTo("William Fernando");
    }
    @DisplayName("Test to find an employee by email")
    @Test
    void findEmployeeByEmail() {
        //Given
        employeeRepository.save(employee);

        //When
        Employee employee1 = employeeRepository.findByEmail(employee.getEmail()).get();

        assertThat(employee1.getName()).isEqualTo("Jose");
    }
}
