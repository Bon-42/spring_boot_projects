package com.dexcom.vacationsapi.controllers;

import com.dexcom.vacationsapi.dto.VacationRequestDTO;
import com.dexcom.vacationsapi.models.VacationRequest;
import com.dexcom.vacationsapi.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/{employeeId}/requests")
    public ResponseEntity<List<VacationRequest>> getVacationRequests(@PathVariable Long employeeId) {
         var vacationRequests =  employeeService.getVacationRequests(employeeId);
         return ResponseEntity.ok(vacationRequests);
    }
    @GetMapping("/{employeeId}/request-status/{status}")
    public ResponseEntity<List<VacationRequest>> getVacationRequests(@PathVariable Long employeeId, @PathVariable String status) {
        List<VacationRequest> vacationRequests = employeeService.getVacationRequestsByStatus(employeeId, status);
        return ResponseEntity.ok(vacationRequests);
    }

    @GetMapping("/{employeeId}/vacation-days-left")
    public ResponseEntity<Integer> getRemainingVacationDays(@PathVariable Long employeeId) {
        int remainingVacationDays = employeeService.getRemainingVacationDays(employeeId);
        return ResponseEntity.ok(remainingVacationDays);
    }

    @PostMapping("/vacation-requests")
    public ResponseEntity<?> createVacationRequest(@RequestBody VacationRequestDTO vacationRequestDTO) {
        try
        {
            VacationRequest vacationRequest = employeeService.createVacationRequest(
                    vacationRequestDTO.getId(),
                    vacationRequestDTO.getNumberOfDays(),
                    vacationRequestDTO.getVacationStartDate()
            );
            // Build the URI for the newly created resource
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(vacationRequest.getId())
                    .toUri();
            return ResponseEntity.created(location).body(vacationRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}