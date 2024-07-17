package com.dexcom.vacationsapi.services;

import com.dexcom.vacationsapi.dto.VacationRequestResponse;
import com.dexcom.vacationsapi.models.Employee;
import com.dexcom.vacationsapi.models.Manager;
import com.dexcom.vacationsapi.models.VacationRequest;
import com.dexcom.vacationsapi.repositories.EmployeeRepository;
import com.dexcom.vacationsapi.repositories.VacationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<VacationRequestResponse> getAllVacationRequests(Long managerId) {
        return vacationRequestRepository.findAll().stream()
                .filter(request -> request.getAuthor().getManager().getId().equals(managerId))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<VacationRequestResponse> getEmployeeVacationOverview(Long managerId, Long employeeId) {
        return vacationRequestRepository.findByAuthorId(employeeId).stream()
                .filter(request -> request.getAuthor().getManager().getId().equals(managerId))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<VacationRequestResponse> getOverlappingRequests(Long managerId, LocalDate startDate, LocalDate endDate) {
        return vacationRequestRepository.findAll().stream()
                .filter(request -> request.getVacationStartDate().isBefore(endDate) && request.getVacationEndDate().isAfter(startDate))
                .filter(request -> request.getAuthor().getManager().getId().equals(managerId))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<VacationRequest> getVacationRequestsByStatus(Long managerId, String status) {
        return vacationRequestRepository.findAllByManagerIdAndStatus(managerId, status);
    }

    @Transactional
    public ResponseEntity<String> processVacationRequest(Long managerId, Long requestId, String status) {
        VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Vacation request not found"));

        if (!"PENDING".equals(vacationRequest.getStatus())) {
            return ResponseEntity.badRequest().body("Request has already been processed and is in \"" + vacationRequest.getStatus() + "\" status.");
        }

        if (!vacationRequest.getAuthor().getManager().getId().equals(managerId)) {
            return ResponseEntity.status(403).body("Manager does not have permission to process this request.");
        }

        // If approved, update the employee's remaining vacation days
        if ("APPROVED".equalsIgnoreCase(status)) {
            Employee employee = vacationRequest.getAuthor();
            int numberOfDays = vacationRequest.getVacationEndDate().getDayOfYear() - vacationRequest.getVacationStartDate().getDayOfYear() + 1;
            employee.setVacationDaysLeft(employee.getVacationDaysLeft() - numberOfDays);
            employeeRepository.save(employee);
        }

        vacationRequest.setStatus(status);
        Manager manager = new Manager();
        manager.setId(managerId);
        vacationRequest.setResolvedBy(manager);

        vacationRequestRepository.save(vacationRequest);
        return ResponseEntity.ok("Request has been successfully processed to \"" + status + "\" status.");
    }

    private VacationRequestResponse convertToResponse(VacationRequest vacationRequest) {
        VacationRequestResponse response = new VacationRequestResponse();
        response.setId(vacationRequest.getId());
        response.setEmployeeName(vacationRequest.getEmployeeName());
        response.setStatus(vacationRequest.getStatus());
        response.setRequestCreatedAt(vacationRequest.getRequestCreatedAt());
        response.setVacationStartDate(vacationRequest.getVacationStartDate());
        response.setVacationEndDate(vacationRequest.getVacationEndDate());
        response.setManagerId(vacationRequest.getAuthor().getManager().getId());
        return response;
    }

}
