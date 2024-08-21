package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userHome(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("activeTab", "user");
        return "user";
    }
}
