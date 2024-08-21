package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("currentUser", currentUser);
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("currentUserEmail", principal.getName());
        model.addAttribute("currentUserRoles", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return "admin";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user, @RequestParam Set<Long> roleIds) {
        Set<Role> roles = roleService.findRolesByIds(roleIds);
        userService.save(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "edit";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam Long id, @ModelAttribute User user, @RequestParam Set<Long> roleIds) {
        user.setId(id);
        Set<Role> roles = roleService.findRolesByIds(roleIds);
        userService.update(user, roles);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
