package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.StatType;
import com.example.SportMonitoring.Repositories.StatTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin_stat_types")
public class AdminStatTypeController {

    @Autowired
    StatTypeRepository statTypeRepository;

    @GetMapping("")
    public String showStatTypes(Model model) {
        model.addAttribute("types", statTypeRepository.findAll());
        return "admin_stat_types";
    }

    @GetMapping("/delete/{id}")
    public String deleteStatType(@PathVariable Long id) {
        statTypeRepository.deleteById(id);
        return "redirect:/admin_stat_types";
    }

    @PostMapping("/add")
    public String addStatType(@RequestParam String name) {
        StatType type = new StatType();
        type.setName(name);
        statTypeRepository.save(type);
        return "redirect:/admin_stat_types";
    }

    @GetMapping("/edit/{id}")
    public String editStatTypeForm(@PathVariable Long id, Model model) {
        Optional<StatType> type = statTypeRepository.findById(id);
        if (type.isPresent()) {
            model.addAttribute("type", type.get());
            return "edit_stat_type";
        }
        return "redirect:/admin_stat_types";
    }

    @PostMapping("/edit/{id}")
    public String editStatType(@PathVariable Long id, @RequestParam String name) {
        Optional<StatType> typeOpt = statTypeRepository.findById(id);
        if (typeOpt.isPresent()) {
            StatType type = typeOpt.get();
            type.setName(name);
            statTypeRepository.save(type);
        }
        return "redirect:/admin_stat_types";
    }
}
