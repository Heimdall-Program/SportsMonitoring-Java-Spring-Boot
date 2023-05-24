package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;

import com.example.SportMonitoring.Repositories.UserRepository;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/view-profile")
    public String showAthleteProfilePage(Model model) {
        List<User> athletes = userRepository.findAll();
        model.addAttribute("users", athletes);
        model.addAttribute("athlete", new User());
        return "view-profile";
    }

    @CrossOrigin
    @GetMapping("/api/user")
    @ResponseBody
    public ResponseEntity<User> getUser(@RequestParam String id) {
        long idLong;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(idLong).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }
}
