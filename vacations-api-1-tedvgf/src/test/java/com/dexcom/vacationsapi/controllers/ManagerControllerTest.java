package com.dexcom.vacationsapi.controllers;

import com.dexcom.vacationsapi.dto.VacationRequestDTO;
import com.dexcom.vacationsapi.dto.VacationRequestResponse;
import com.dexcom.vacationsapi.models.VacationRequest;
import com.dexcom.vacationsapi.services.ManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ManagerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerController managerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }

    @Test
    public void testGetAllVacationRequests() throws Exception {
        Long managerId = 1L;
        List<VacationRequestResponse> mockRequests = Arrays.asList(new VacationRequestResponse());
        when(managerService.getAllVacationRequests(managerId)).thenReturn(mockRequests);

        mockMvc.perform(get("/v1/api/managers/{managerId}/vacation-requests", managerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetVacationRequestsByStatus() throws Exception {
        Long managerId = 1L;
        String status = "APPROVED";
        List<VacationRequest> mockRequests = Arrays.asList(new VacationRequest());
        when(managerService.getVacationRequestsByStatus(managerId, status)).thenReturn(mockRequests);

        mockMvc.perform(get("/v1/api/managers/{managerId}/status/{status}", managerId, status)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEmployeeVacationOverview() throws Exception {
        Long managerId = 1L;
        Long employeeId = 1L;
        List<VacationRequestResponse> mockRequests = Arrays.asList(new VacationRequestResponse());
        when(managerService.getEmployeeVacationOverview(managerId, employeeId)).thenReturn(mockRequests);

        mockMvc.perform(get("/v1/api/managers/{managerId}/employee/{employeeId}/overview", managerId, employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOverlappingRequests() throws Exception {
        Long managerId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        List<VacationRequestResponse> mockRequests = Arrays.asList(new VacationRequestResponse());
        when(managerService.getOverlappingRequests(managerId, startDate, endDate)).thenReturn(mockRequests);

        mockMvc.perform(get("/v1/api/managers/{managerId}/vacation-requests/overlapping", managerId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testProcessVacationRequest() throws Exception {
        Long managerId = 1L;
        Long requestId = 1L;
        VacationRequestDTO requestDTO = new VacationRequestDTO();
        requestDTO.setStatus("APPROVED");

        when(managerService.processVacationRequest(managerId, requestId, requestDTO.getStatus()))
                .thenReturn(ResponseEntity.ok("Request has been successfully processed to \"APPROVED\" status."));

        mockMvc.perform(post("/v1/api/managers/{managerId}/vacation-requests/{requestId}/process", managerId, requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"APPROVED\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testProcessVacationRequestForbidden() throws Exception {
        Long managerId = 1L;
        Long requestId = 1L;
        VacationRequestDTO requestDTO = new VacationRequestDTO();
        requestDTO.setStatus("APPROVED");

        when(managerService.processVacationRequest(managerId, requestId, requestDTO.getStatus()))
                .thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Manager does not have permission to process this request."));

        mockMvc.perform(post("/v1/api/managers/{managerId}/vacation-requests/{requestId}/process", managerId, requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"APPROVED\"}"))
                .andExpect(status().isForbidden());
    }
}