package com.dexcom.vacationsapi.controllers;

import com.dexcom.vacationsapi.dto.VacationRequestDTO;
import com.dexcom.vacationsapi.dto.VacationRequestResponse;
import com.dexcom.vacationsapi.models.VacationRequest;
import com.dexcom.vacationsapi.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/api/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/{managerId}/vacation-requests")
    public ResponseEntity<List<VacationRequestResponse>> getAllVacationRequests(@PathVariable Long managerId) {
        List<VacationRequestResponse> vacationRequests = managerService.getAllVacationRequests(managerId);
        return ResponseEntity.ok(vacationRequests);
    }

    @GetMapping("/{managerId}/status/{status}")
    public ResponseEntity<List<VacationRequest>> getVacationRequests(@PathVariable Long managerId, @PathVariable String status) {
        List<VacationRequest> vacationRequests = managerService.getVacationRequestsByStatus(managerId, status);
        return ResponseEntity.ok(vacationRequests);
    }

    @GetMapping("/{managerId}/employee/{employeeId}/overview")
    public ResponseEntity<List<VacationRequestResponse>> getEmployeeVacationOverview(@PathVariable Long managerId, @PathVariable Long employeeId) {
        List<VacationRequestResponse> vacationRequests = managerService.getEmployeeVacationOverview(managerId, employeeId);
        return ResponseEntity.ok(vacationRequests);
    }

    @GetMapping("/{managerId}/vacation-requests/overlapping")
    public ResponseEntity<List<VacationRequestResponse>> getOverlappingRequests(@PathVariable Long managerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<VacationRequestResponse> vacationRequests = managerService.getOverlappingRequests(managerId, startDate, endDate);
        return ResponseEntity.ok(vacationRequests);
    }
    @PostMapping("/{managerId}/vacation-requests/{requestId}/process")
    public ResponseEntity<String> processVacationRequest(@PathVariable Long managerId, @PathVariable Long requestId, @RequestBody VacationRequestDTO request) {
        return managerService.processVacationRequest(managerId, requestId, request.getStatus());
    }
}
