package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.Stat;
import com.example.SportMonitoring.Models.StatType;
import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Repositories.StatRepository;
import com.example.SportMonitoring.Repositories.StatTypeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StatController {

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private StatTypeRepository statTypeRepository;

    @GetMapping("/stats")
    public String showStats(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<StatType> types = statTypeRepository.findAll();
        model.addAttribute("types", types);
        model.addAttribute("showGraph", false);
        return "stats";
    }

    @GetMapping("/stats/{typeId}")
    public String showStats(@PathVariable Long typeId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        StatType selectedType = statTypeRepository.findById(typeId).orElse(null);
        if (selectedType == null) {
            return "redirect:/stats";
        }

        List<Stat> stats = statRepository.findByUser(user).stream()
                .filter(stat -> stat.getType().getId().equals(typeId))
                .collect(Collectors.toList());

        model.addAttribute("stats", stats);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        List<String> statDates = stats.stream()
                .map(stat -> formatter.format(stat.getAssignedDate()))
                .collect(Collectors.toList());

        List<Double> statValues = stats.stream()
                .map(Stat::getValue)
                .collect(Collectors.toList());

        List<String> statNames = stats.stream()
                .map(Stat::getName)
                .collect(Collectors.toList());

        model.addAttribute("statNames", statNames);
        model.addAttribute("statDates", statDates);
        model.addAttribute("statValues", statValues);
        model.addAttribute("showGraph", !stats.isEmpty());

        return "stats";
    }

}
