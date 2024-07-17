package com.dexcom.vacationsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "DTO for creating a vacation request")
public class VacationRequestDTO {
    private Long id;
    private int numberOfDays;
    private LocalDate vacationStartDate;
    private String status;
}
