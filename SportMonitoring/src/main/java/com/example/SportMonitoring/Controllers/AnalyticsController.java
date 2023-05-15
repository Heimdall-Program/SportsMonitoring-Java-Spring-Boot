package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.Athlete;
import com.example.SportMonitoring.Models.PerformanceIndicator;
import com.example.SportMonitoring.Repositories.AthleteRepository;
import com.example.SportMonitoring.Repositories.PerformanceIndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AnalyticsController {

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private PerformanceIndicatorRepository performanceIndicatorRepository;

    @GetMapping("/analytics")
    public String analyticsForm(Model model) {
        List<Athlete> athletes = athleteRepository.findAll();
        model.addAttribute("athletes", athletes);
        return "analytics";
    }

    @PostMapping("/analytics")
    public String generateReport(@RequestParam String athleteName, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Model model) {
        Athlete athlete = athleteRepository.findByName(athleteName);
        List<PerformanceIndicator> indicators = performanceIndicatorRepository.findByAthleteAndDateBetween(athlete, startDate, endDate);
        model.addAttribute("indicators", indicators);
        return "report";
    }
}
