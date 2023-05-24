package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorisationController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public String registerForm(Model model) {
        return "registration";
    }

    @PostMapping("/register")
    public String register(Model model, @RequestParam String name, @RequestParam String surname, @RequestParam int age,
                           @RequestParam String gender, @RequestParam String login, @RequestParam String password,
                           @RequestParam String confirm_password) {

        if (!password.equals(confirm_password)) {
            model.addAttribute("error", "Пароли не совпадают!");
            return "registration";
        }

        User user = userRepository.findByLogin(login);
        if (user != null) {
            model.addAttribute("error", "Пользователь уже существует!");
            return "registration";
        }

        user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setAge(age);
        user.setGender(gender);
        user.setLogin(login);
        user.setPassword(password);
        user.setRole("user");

        userRepository.save(user);

        return "login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpSession session, Model model, @RequestParam String login, @RequestParam String password) {

        User user = userRepository.findByLogin(login);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Неверный логин и/или пароль!");
            return "login";
        }

        session.setAttribute("user", user);

        String role = user.getRole();
        if (role.equals("admin")) {
            return "redirect:/users";
        } else if (role.equals("user")) {
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Неизвестная роль пользователя!");
            return "login";
        }
    }

}


