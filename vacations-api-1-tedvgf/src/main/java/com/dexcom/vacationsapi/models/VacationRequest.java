package com.dexcom.vacationsapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private Employee author;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private int numberOfVacationDaysRequested;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_id")
    @JsonIgnore
    private Manager resolvedBy;

    @Column(nullable = false)
    private LocalDateTime requestCreatedAt;

    @Column(nullable = false)
    private LocalDate vacationStartDate;

    @Column(nullable = false)
    private LocalDate vacationEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    @JsonIgnore
    private Manager manager;
}
