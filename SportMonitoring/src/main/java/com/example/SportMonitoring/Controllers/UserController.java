package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "profile";
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/login";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam("picture") MultipartFile picture,
                                @RequestParam("name") String name,
                                @RequestParam("surname") String surname,
                                @RequestParam("age") int age,
                                @RequestParam("gender") String gender,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");

        if (!picture.isEmpty()) {
            String pictureUrl = uploadPicture(picture);
            user.setPictureUrl(pictureUrl);
        }

        user.setName(name);
        user.setSurname(surname);
        user.setAge(age);
        user.setGender(gender);

        userRepository.save(user);

        session.setAttribute("user", user);

        redirectAttributes.addFlashAttribute("message", "Профиль успешно обновлен!");

        return "redirect:/profile";
    }

    private String uploadPicture(MultipartFile picture) {
        try {
            if (picture != null && !picture.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));
                Path path = Paths.get("src/main/resources/static/uploads/" + fileName);
                Files.copy(picture.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                return "/uploads/" + fileName;
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения картинки: " + e.getMessage());
        }
        return null;
    }

}
