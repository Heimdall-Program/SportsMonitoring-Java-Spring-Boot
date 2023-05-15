package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.Athlete;
import com.example.SportMonitoring.Repositories.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AthleteController {

    @Autowired
    private AthleteRepository athleteRepository;

    @GetMapping("/addAthlete")
    public String showAddAthleteForm(Athlete athlete) {
        return "addAthlete";
    }

    @PostMapping("/addAthlete")
    public String saveAthlete(Athlete athlete) {
        athleteRepository.save(athlete);
        return "redirect:/";
    }
}
