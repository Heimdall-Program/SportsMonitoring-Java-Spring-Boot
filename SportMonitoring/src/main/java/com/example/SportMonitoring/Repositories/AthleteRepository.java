package com.example.SportMonitoring.Repositories;

import com.example.SportMonitoring.Models.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AthleteRepository extends JpaRepository<Athlete, Integer> {
    Athlete findByName(String name);

}

