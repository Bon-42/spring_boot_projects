package com.dexcom.vacationsapi.repositories;

import com.dexcom.vacationsapi.models.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    List<VacationRequest> findByAuthorId(Long authorId);
    List<VacationRequest> findAllByAuthorIdAndStatus(Long authorId, String status);
    List<VacationRequest> findAllByManagerIdAndStatus(Long managerid, String status);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.author.id = :authorId AND vr.status = 'PENDING' AND " +
            "(vr.vacationStartDate BETWEEN :startDate AND :endDate OR vr.vacationEndDate BETWEEN :startDate AND :endDate OR " +
            "(vr.vacationStartDate <= :startDate AND vr.vacationEndDate >= :endDate))")
    List<VacationRequest> findOverlappingRequests(@Param("authorId") Long authorId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
