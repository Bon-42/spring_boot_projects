package com.dexcom.vacationsapi.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VacationRequestResponse {
    private Long id;
    private String employeeName;
    private String status;
    private LocalDateTime requestCreatedAt;
    private LocalDate vacationStartDate;
    private LocalDate vacationEndDate;
    private Long managerId;
}
