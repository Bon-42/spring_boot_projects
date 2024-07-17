package com.dexcom.vacationsapi.controllers;

import com.dexcom.vacationsapi.dto.VacationRequestDTO;
import com.dexcom.vacationsapi.models.VacationRequest;
import com.dexcom.vacationsapi.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testGetVacationRequests() throws Exception {
        Long employeeId = 1L;
        List<VacationRequest> mockRequests = Arrays.asList(new VacationRequest());
        when(employeeService.getVacationRequests(employeeId)).thenReturn(mockRequests);

        mockMvc.perform(get("/v1/api/employee/{employeeId}/requests", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetVacationRequestsByStatus() throws Exception {
        Long employeeId = 1L;
        String status = "APPROVED";
        List<VacationRequest> mockRequests = Arrays.asList(new VacationRequest());
        when(employeeService.getVacationRequestsByStatus(employeeId, status)).thenReturn(mockRequests);

        mockMvc.perform(get("/v1/api/employee/{employeeId}/status/{status}", employeeId, status)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRemainingVacationDays() throws Exception {
        Long employeeId = 1L;
        int remainingDays = 10;
        when(employeeService.getRemainingVacationDays(employeeId)).thenReturn(remainingDays);

        mockMvc.perform(get("/v1/api/employee/{employeeId}/vacation-days-left", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateVacationRequestSuccess() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setId(1L);
        vacationRequestDTO.setNumberOfDays(5);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());

        VacationRequest mockRequest = new VacationRequest();
        mockRequest.setId(1L);

        when(employeeService.createVacationRequest(any(Long.class), any(int.class), any(LocalDate.class))).thenReturn(mockRequest);

        mockMvc.perform(post("/v1/api/employee/vacation-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"numberOfDays\":5,\"vacationStartDate\":\"" + LocalDate.now().toString() + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateVacationRequestBadRequest() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setId(1L);
        vacationRequestDTO.setNumberOfDays(5);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());

        when(employeeService.createVacationRequest(any(Long.class), any(int.class), any(LocalDate.class)))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        mockMvc.perform(post("/v1/api/employee/vacation-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"numberOfDays\":5,\"vacationStartDate\":\"" + LocalDate.now().toString() + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateVacationRequestServerError() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setId(1L);
        vacationRequestDTO.setNumberOfDays(5);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());

        when(employeeService.createVacationRequest(any(Long.class), any(int.class), any(LocalDate.class)))
                .thenThrow(new RuntimeException("Server error"));

        mockMvc.perform(post("/v1/api/employee/vacation-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"numberOfDays\":5,\"vacationStartDate\":\"" + LocalDate.now().toString() + "\"}"))
                .andExpect(status().isInternalServerError());
    }
}
