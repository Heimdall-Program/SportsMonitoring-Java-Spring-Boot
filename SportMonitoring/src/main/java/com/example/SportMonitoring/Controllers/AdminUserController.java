package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminUserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add_user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Чёрт: " + id));
        model.addAttribute("user", user);
        return "edit_user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, User updatedUser, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getId().equals(id)) {
            model.addAttribute("errorMessage", "Вы не можете редактировать свой профиль");
            return "users";
        }

        if (updatedUser.getAge() < 12 || updatedUser.getAge() > 70) {
            model.addAttribute("errorMessage", "Возраст должен быть в диапазоне от 12 до 70");
            return "users";
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        user.setLogin(updatedUser.getLogin());
        user.setPassword(updatedUser.getPassword());
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setAge(updatedUser.getAge());
        user.setGender(updatedUser.getGender());
        user.setRole(updatedUser.getRole());

        if (user.getStats() != null) {
            user.getStats().clear();
            if (updatedUser.getStats() != null) {
                user.getStats().addAll(updatedUser.getStats());
            }
        } else {
            user.setStats(updatedUser.getStats());
        }

        userRepository.save(user);

        model.addAttribute("users", userRepository.findAll());
        return "users";
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }
}
