package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.Stat;
import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Repositories.StatRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class StatController {

    @Autowired
    private StatRepository statRepository;

    @GetMapping("/stats")
    public String showStats(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Stat> stats = statRepository.findByUser(user);
        model.addAttribute("stats", stats);
        return "stats";
    }

    @GetMapping("/stats/detail/{id}")
    public String showStatDetail(@PathVariable Long id, Model model, HttpSession session) {
        Stat stat = statRepository.findById(id).orElse(null);
        if (stat == null) {
            return "redirect:/error";
        }
        model.addAttribute("stat", stat);
        return "detail";
    }

}
