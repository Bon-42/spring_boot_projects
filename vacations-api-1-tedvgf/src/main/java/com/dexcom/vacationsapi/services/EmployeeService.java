package com.dexcom.vacationsapi.services;

import com.dexcom.vacationsapi.models.Employee;
import com.dexcom.vacationsapi.models.VacationRequest;
import com.dexcom.vacationsapi.repositories.EmployeeRepository;
import com.dexcom.vacationsapi.repositories.VacationRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    public List<VacationRequest> getVacationRequests(Long employeeId) {
        return vacationRequestRepository.findByAuthorId(employeeId);
    }

    public List<VacationRequest> getVacationRequestsByStatus(Long employeeId, String status) {
        return vacationRequestRepository.findAllByAuthorIdAndStatus(employeeId, status);
    }

    public int getRemainingVacationDays(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(Employee::getVacationDaysLeft)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Transactional
    public VacationRequest createVacationRequest(Long id, int numberOfDays, LocalDate vacationStartDate) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        int vacationDaysLeft = employee.getVacationDaysLeft();

        if (vacationDaysLeft < numberOfDays) {
            throw new IllegalArgumentException("Employee doesn't have enough vacation days left");
        }

        LocalDate vacationEndDate = vacationStartDate.plusDays(numberOfDays - 1);

        // Check for duplicate requests
        List<VacationRequest> overlappingRequests = vacationRequestRepository.findOverlappingRequests(id, vacationStartDate, vacationEndDate);
        if (!overlappingRequests.isEmpty()) {
            throw new IllegalArgumentException("A vacation request for these dates already exists");
        }

        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setAuthor(employee);
        vacationRequest.setEmployeeName(employee.getName());
        vacationRequest.setNumberOfVacationDaysRequested(numberOfDays);
        vacationRequest.setStatus("PENDING");
        vacationRequest.setManager(employee.getManager());
        vacationRequest.setRequestCreatedAt(LocalDateTime.now());
        vacationRequest.setVacationStartDate(vacationStartDate);
        vacationRequest.setVacationEndDate(vacationStartDate.plusDays(numberOfDays - 1));

        employeeRepository.save(employee);

        return vacationRequestRepository.save(vacationRequest);
    }
}