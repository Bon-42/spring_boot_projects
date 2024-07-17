package com.dexcom.vacationsapi.services;

import com.dexcom.vacationsapi.dto.VacationRequestResponse;
import com.dexcom.vacationsapi.models.Employee;
import com.dexcom.vacationsapi.models.Manager;
import com.dexcom.vacationsapi.models.VacationRequest;
import com.dexcom.vacationsapi.repositories.EmployeeRepository;
import com.dexcom.vacationsapi.repositories.VacationRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ManagerServiceTest {

    @Mock
    private VacationRequestRepository vacationRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ManagerService managerService;

    private Employee employee;
    private Manager manager;
    private VacationRequest vacationRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        manager = new Manager();
        manager.setId(1L);
        manager.setName("Jane Smith");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setVacationDaysLeft(20);
        employee.setManager(manager);

        vacationRequest = new VacationRequest();
        vacationRequest.setId(1L);
        vacationRequest.setAuthor(employee);
        vacationRequest.setEmployeeName(employee.getName());
        vacationRequest.setNumberOfVacationDaysRequested(5);
        vacationRequest.setStatus("PENDING");
        vacationRequest.setManager(manager);
        vacationRequest.setRequestCreatedAt(LocalDateTime.now());
        vacationRequest.setVacationStartDate(LocalDate.now());
        vacationRequest.setVacationEndDate(LocalDate.now().plusDays(4));
    }

    @Test
    public void testGetAllVacationRequests() {
        when(vacationRequestRepository.findAll()).thenReturn(Collections.singletonList(vacationRequest));
        List<VacationRequestResponse> result = managerService.getAllVacationRequests(manager.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vacationRequest.getEmployeeName(), result.get(0).getEmployeeName());
    }

    @Test
    public void testGetEmployeeVacationOverview() {
        when(vacationRequestRepository.findByAuthorId(employee.getId())).thenReturn(Collections.singletonList(vacationRequest));
        List<VacationRequestResponse> result = managerService.getEmployeeVacationOverview(manager.getId(), employee.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vacationRequest.getEmployeeName(), result.get(0).getEmployeeName());
    }

    @Test
    public void testGetOverlappingRequests() {
        when(vacationRequestRepository.findAll()).thenReturn(Collections.singletonList(vacationRequest));
        List<VacationRequestResponse> result = managerService.getOverlappingRequests(manager.getId(), LocalDate.now().minusDays(1), LocalDate.now().plusDays(5));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vacationRequest.getEmployeeName(), result.get(0).getEmployeeName());
    }

    @Test
    public void testProcessVacationRequest_Success() {
        when(vacationRequestRepository.findById(vacationRequest.getId())).thenReturn(Optional.of(vacationRequest));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(vacationRequestRepository.save(any(VacationRequest.class))).thenReturn(vacationRequest);

        ResponseEntity<String> response = managerService.processVacationRequest(manager.getId(), vacationRequest.getId(), "APPROVED");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Request has been successfully processed to \"APPROVED\" status.", response.getBody());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(vacationRequestRepository, times(1)).save(any(VacationRequest.class));
    }

    @Test
    public void testProcessVacationRequest_AlreadyProcessed() {
        vacationRequest.setStatus("APPROVED");
        when(vacationRequestRepository.findById(vacationRequest.getId())).thenReturn(Optional.of(vacationRequest));

        ResponseEntity<String> response = managerService.processVacationRequest(manager.getId(), vacationRequest.getId(), "REJECTED");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Request has already been processed and is in \"APPROVED\" status.", response.getBody());
    }

    @Test
    public void testProcessVacationRequest_Forbidden() {
        Manager anotherManager = new Manager();
        anotherManager.setId(2L);
        vacationRequest.getAuthor().setManager(anotherManager);
        when(vacationRequestRepository.findById(vacationRequest.getId())).thenReturn(Optional.of(vacationRequest));

        ResponseEntity<String> response = managerService.processVacationRequest(manager.getId(), vacationRequest.getId(), "APPROVED");

        assertNotNull(response);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Manager does not have permission to process this request.", response.getBody());
    }

    @Test
    public void testGetVacationRequestsByStatus() {
        when(vacationRequestRepository.findAllByManagerIdAndStatus(manager.getId(), "PENDING")).thenReturn(Collections.singletonList(vacationRequest));
        List<VacationRequest> result = managerService.getVacationRequestsByStatus(manager.getId(), "PENDING");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }
}
