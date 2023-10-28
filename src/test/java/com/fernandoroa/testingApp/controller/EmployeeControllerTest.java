package com.fernandoroa.testingApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernandoroa.testingApp.model.entities.Employee;
import com.fernandoroa.testingApp.service.implementations.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeeControllerTest {


    //Nos permite hacer pruebas HTTP
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImpl employeeService;

    //Permite convertir objetos java en JSON y viseversa
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveEmployee() throws Exception {
        //Given
        Employee employee = Employee.builder()
                .name("Jose")
                .lastname("Vela")
                .email("jose@gmail.com")
                .build();


        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer( invocation ->  invocation.getArgument(0));
        //When
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //Then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(employee.getName())))
                .andExpect(jsonPath("$.lastname", is(employee.getLastname())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void listEmployees() throws Exception {
        //Given
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder().name("Chritian").lastname("Ramirez").email("cris1@gmail.com").build());
        employees.add(Employee.builder().name("Gabriel").lastname("Ramirez").email("g1@gmail.com").build());
        employees.add(Employee.builder().name("Julen").lastname("Ramirez").email("cris1@gmail.com").build());
        employees.add(Employee.builder().name("Biaggio").lastname("Ramirez").email("cris1@gmail.com").build());
        employees.add(Employee.builder().name("Andrian").lastname("Ramirez").email("cris1@gmail.com").build());

        given(employeeService.getAllEmployee()).willReturn(employees);
        //When
        ResultActions response = mockMvc.perform(get("/api/employees"));
        //Then

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(employees.size())));
    }

    @Test
    void getEmployeeById() throws Exception {
        //Given
        long employeeId = 3L;
        Employee employee = Employee.builder()
                .name("Jose")
                .lastname("Vela")
                .email("jose@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //When
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(employee.getName())));
    }

    @Test
    void notFoundEmployee() throws Exception {
        //Given
        long employeeId = 3L;


        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //When
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //Then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateEmployee() throws Exception {
        //Given
        long employeeId = 3L;
        Employee savedEmployee = Employee.builder()
                .name("Jose")
                .lastname("Vela")
                .email("jose@gmail.com")
                .build();

        Employee employeeToUpdate =  Employee.builder()
                .name("Chritian")
                .lastname("Ramirez")
                .email("jose@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer( invocation ->  invocation.getArgument(0));
        //When
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeToUpdate)));
        //Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.email",is(employeeToUpdate.getEmail())))
                .andExpect(jsonPath("$.name",is(employeeToUpdate.getName())));
    }

    @Test
    void updateNonexistentEmployee() throws Exception {
        //Given
        long employeeId = 3L;

        Employee employeeToUpdate =  Employee.builder()
                .name("Chritian")
                .lastname("Ramirez")
                .email("jose@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer( invocation ->  invocation.getArgument(0));
        //When
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeToUpdate)));
        //Then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteEmployee() throws Exception {
        //Given
        long employeeId = 3L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        //When
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}",employeeId));
        //Then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}