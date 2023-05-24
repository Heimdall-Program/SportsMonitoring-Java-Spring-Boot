package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.Rating;
import com.example.SportMonitoring.Models.Stat;
import com.example.SportMonitoring.Models.StatType;
import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Repositories.RatingRepository;
import com.example.SportMonitoring.Repositories.StatRepository;
import com.example.SportMonitoring.Repositories.StatTypeRepository;
import com.example.SportMonitoring.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Autowired
    RatingRepository ratingRepository;

    @GetMapping("/admin_stats")
    public String showStats(Model model) {

        model.addAttribute("stat", new Stat());
        model.addAttribute("types", statTypeRepository.findAll());
        model.addAttribute("users", userRepository.findAll().stream()
                .filter(user -> user.getRole().equalsIgnoreCase("user"))
                .collect(Collectors.toList()));
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


    @PostMapping("/admin_stats/addStat")
    public String addStat(@ModelAttribute Stat stat, @RequestParam Long userId, @RequestParam Long typeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        StatType type = statTypeRepository.findById(typeId).orElseThrow(() -> new RuntimeException("StatType not found"));

        stat.setUser(user);
        stat.setType(type);

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
            userRepository.findById(userId).ifPresent(stat::setUser);
            statRepository.save(stat);
        }
        return "redirect:/admin_stats";
    }

    @GetMapping("/rating")
    public String rating(@RequestParam(name = "typeId", required = false) Long typeId, Model model) {
        List<StatType> types = statTypeRepository.findAll();
        model.addAttribute("types", types);

        if (typeId != null) {
            List<Stat> stats = statRepository.findByTypeIdOrderByValueDesc(typeId);
            model.addAttribute("stats", stats);
        }

        return "rating";
    }

    @PostMapping("/rating/save")
    public String saveRating(@RequestBody List<Rating> ratings) {
        ratingRepository.saveAll(ratings);
        return "redirect:/admin/rating";
    }
}
