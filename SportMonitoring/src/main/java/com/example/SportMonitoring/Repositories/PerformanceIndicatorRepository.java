package com.example.SportMonitoring.Repositories;

import com.example.SportMonitoring.Models.Athlete;
import com.example.SportMonitoring.Models.PerformanceIndicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PerformanceIndicatorRepository extends JpaRepository<PerformanceIndicator, Integer> {
    List<PerformanceIndicator> findByAthleteAndDateBetween(Athlete athlete, LocalDate startDate, LocalDate endDate);
}
