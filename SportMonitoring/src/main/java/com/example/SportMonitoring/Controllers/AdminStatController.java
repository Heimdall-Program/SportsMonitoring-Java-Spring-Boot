package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.Stat;
import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Repositories.StatRepository;
import com.example.SportMonitoring.Repositories.StatTypeRepository;
import com.example.SportMonitoring.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdminStatController {

    @Autowired
    StatRepository statRepository;

    @Autowired
    StatTypeRepository statTypeRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/admin_stats")
    public String showStats(Model model) {
        model.addAttribute("stats", statRepository.findAll());
        return "admin_stats";
    }

    @GetMapping("/admin_stats/delete/{id}")
    public String deleteStat(@PathVariable Long id) {
        statRepository.deleteById(id);
        return "redirect:/admin_stats";
    }

    @GetMapping("/admin_stats/add")
    public String addStatForm(Model model) {
        model.addAttribute("stat", new Stat());
        model.addAttribute("types", statTypeRepository.findAll());
        model.addAttribute("users", userRepository.findAll().stream()
                .filter(user -> user.getRole().equalsIgnoreCase("user"))
                .collect(Collectors.toList()));
        model.addAttribute("stats", statRepository.findAll());
        return "add_stat";
    }


    @PostMapping("/admin_stats/add")
    public String addStat(@RequestParam String name, @RequestParam String description, @RequestParam Double value, @RequestParam Long typeId, @RequestParam Long userId) {
        Stat stat = new Stat(name, description, value);
        stat.setType(statTypeRepository.findById(typeId).orElse(null));
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            stat.setUser(user);
        }
        statRepository.save(stat);
        return "redirect:/admin_stats";
    }

    @GetMapping("/admin_stats/edit/{id}")
    public String editStatForm(@PathVariable Long id, Model model) {
        Optional<Stat> stat = statRepository.findById(id);
        if (stat.isPresent()) {
            model.addAttribute("stat", stat.get());
            model.addAttribute("types", statTypeRepository.findAll());
            model.addAttribute("users", userRepository.findAll().stream()
                    .filter(user -> user.getRole().equalsIgnoreCase("user"))
                    .collect(Collectors.toList()));
            return "edit_stat";
        }
        return "redirect:/admin_stats";
    }


    @PostMapping("/admin_stats/edit/{id}")
    public String editStat(@PathVariable Long id, @RequestParam String name, @RequestParam String description, @RequestParam Double value, @RequestParam Long typeId, @RequestParam Long userId) {
        Optional<Stat> statOpt = statRepository.findById(id);
        if (statOpt.isPresent()) {
            Stat stat = statOpt.get();
            stat.setName(name);
            stat.setDescription(description);
            stat.setValue(value);
            stat.setType(statTypeRepository.findById(typeId).orElse(null));
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                stat.setUser(user);
            }
            statRepository.save(stat);
        }
        return "redirect:/admin_stats";
    }
}
