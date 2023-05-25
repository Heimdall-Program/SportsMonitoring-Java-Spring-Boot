package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.Stat;
import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Models.StatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping
    public String analysisForm(Model model) {
        model.addAttribute("users", analysisService.getAllUsers());
        model.addAttribute("statTypes", analysisService.getAllStatTypes());
        model.addAttribute("user", new User());
        model.addAttribute("statType", new StatType());

        return "analysis";
    }

    @PostMapping
    public String submitAnalysis(@RequestParam("userId") Optional<Long> userId, @RequestParam("statTypeId") Optional<Long> statTypeId, Model model) {
        User user = userId.map(aLong -> analysisService.findUserById(aLong)).orElse(null);
        StatType statType = statTypeId.map(aLong -> analysisService.findStatTypeById(aLong)).orElse(null);

        List<Stat> stats = analysisService.getStats(user, statType);
        model.addAttribute("stats", stats);

        double progressAnalysis = analysisService.calculateProgress(stats);
        model.addAttribute("progressAnalysis", progressAnalysis);

        double futureProjection = analysisService.calculateProjection(stats);
        model.addAttribute("futureProjection", futureProjection);

        return "analysis";
    }
}
