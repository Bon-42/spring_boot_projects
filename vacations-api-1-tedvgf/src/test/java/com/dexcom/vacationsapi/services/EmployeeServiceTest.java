package com.dexcom.vacationsapi.services;

import com.dexcom.vacationsapi.models.Employee;
import com.dexcom.vacationsapi.models.VacationRequest;
import com.dexcom.vacationsapi.repositories.EmployeeRepository;
import com.dexcom.vacationsapi.repositories.VacationRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private VacationRequestRepository vacationRequestRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private VacationRequest vacationRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setVacationDaysLeft(20);

        vacationRequest = new VacationRequest();
        vacationRequest.setId(1L);
        vacationRequest.setAuthor(employee);
        vacationRequest.setEmployeeName(employee.getName());
        vacationRequest.setNumberOfVacationDaysRequested(5);
        vacationRequest.setStatus("PENDING");
        vacationRequest.setManager(employee.getManager());
        vacationRequest.setRequestCreatedAt(LocalDateTime.now());
        vacationRequest.setVacationStartDate(LocalDate.now());
        vacationRequest.setVacationEndDate(LocalDate.now().plusDays(4));
    }

    @Test
    public void testGetVacationRequests() {
        when(vacationRequestRepository.findByAuthorId(employee.getId())).thenReturn(Collections.singletonList(vacationRequest));
        List<VacationRequest> result = employeeService.getVacationRequests(employee.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vacationRequest, result.get(0));
    }

    @Test
    public void testGetVacationRequestsByStatus() {
        when(vacationRequestRepository.findAllByAuthorIdAndStatus(employee.getId(), "PENDING")).thenReturn(Collections.singletonList(vacationRequest));
        List<VacationRequest> result = employeeService.getVacationRequestsByStatus(employee.getId(), "PENDING");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vacationRequest, result.get(0));
    }

    @Test
    public void testGetRemainingVacationDays() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        int remainingDays = employeeService.getRemainingVacationDays(employee.getId());

        assertEquals(20, remainingDays);
    }

    @Test
    public void testGetRemainingVacationDays_EmployeeNotFound() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.getRemainingVacationDays(employee.getId());
        });

        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    public void testCreateVacationRequest_Success() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(vacationRequestRepository.findOverlappingRequests(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(vacationRequestRepository.save(any(VacationRequest.class))).thenReturn(vacationRequest);

        VacationRequest result = employeeService.createVacationRequest(employee.getId(), 5, LocalDate.now());

        assertNotNull(result);
        assertEquals("John Doe", result.getEmployeeName());
        assertEquals("PENDING", result.getStatus());
        verify(employeeRepository, times(1)).save(employee);
        verify(vacationRequestRepository, times(1)).save(any(VacationRequest.class));
    }

    @Test
    public void testCreateVacationRequest_NotEnoughDays() {
        employee.setVacationDaysLeft(3);
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createVacationRequest(employee.getId(), 5, LocalDate.now());
        });

        assertEquals("Employee doesn't have enough vacation days left", exception.getMessage());
    }

    @Test
    public void testCreateVacationRequest_DuplicateRequest() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(vacationRequestRepository.findOverlappingRequests(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(vacationRequest));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createVacationRequest(employee.getId(), 5, LocalDate.now());
        });

        assertEquals("A vacation request for these dates already exists", exception.getMessage());
    }
}