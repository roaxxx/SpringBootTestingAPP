package com.fernandoroa.testingApp.controller;

import com.fernandoroa.testingApp.model.entities.Employee;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTestRestTemplate {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    void saveEmployeeTest() {
        Employee mockEmployee = Employee.builder()
                .id(1L)
                .name("Jose")
                .lastname("Vela")
                .email("jose@gmail.com")
                .build();

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(
                "http://localhost:8081/api/employees",
                mockEmployee,
                Employee.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);

        Employee savedEmployee = response.getBody();
        assertNotNull(savedEmployee);
        assertEquals(savedEmployee.getName(),mockEmployee.getName());
        assertEquals(savedEmployee.getLastname(),mockEmployee.getLastname());
        assertEquals(savedEmployee.getEmail(),mockEmployee.getEmail());
    }

    @Test
    @Order(2)
    void listEmployeesTest(){
        ResponseEntity<Employee[]> response = testRestTemplate.getForEntity(
                "http://localhost:8081/api/employees",
                Employee[].class
        );
        List<Employee> employees = Arrays.asList(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);

        assertEquals(1, employees.size());
        assertEquals(1L, employees.get(0).getId());
        assertEquals("jose@gmail.com", employees.get(0).getEmail());
    }

    @Test
    @Order(3)
    void getEmployeeByIdTest() {
        ResponseEntity<Employee> response = testRestTemplate.getForEntity(
                "http://localhost:8081/api/employees/1",
                Employee.class
        );

        Employee employee = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        assertNotNull(employee);
        assertEquals(employee.getName(),"Jose");
        assertEquals(employee.getLastname(),"Vela");
        assertEquals(employee.getEmail(),"jose@gmail.com");
    }

    @Test
    @Order(4)
    void deleteEmployeeTest() {
        ResponseEntity<Employee[]> response = testRestTemplate.getForEntity(
                "http://localhost:8081/api/employees",
                Employee[].class
        );
        List<Employee> employees = Arrays.asList(response.getBody());

        assertEquals(1, employees.size());

        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id",2L);

        ResponseEntity<Void> exchange = testRestTemplate.exchange(
                "http://localhost:8081/api/employees/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                pathVariables
        );

        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertFalse(exchange.hasBody());

        response = testRestTemplate.getForEntity(
                "http://localhost:8081/api/employees",
                Employee[].class
        );
        employees =  Arrays.asList(response.getBody());
        assertEquals(0, employees.size());
    }

    void updateEmployeeTest(){

    }

}
