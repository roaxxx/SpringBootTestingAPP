package com.fernandoroa.testingApp.repositories;

import com.fernandoroa.testingApp.model.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
}
