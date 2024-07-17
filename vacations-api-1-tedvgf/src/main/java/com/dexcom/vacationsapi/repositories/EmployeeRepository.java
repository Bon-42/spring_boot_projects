package com.dexcom.vacationsapi.repositories;

import com.dexcom.vacationsapi.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findById(Long employeeId);
}