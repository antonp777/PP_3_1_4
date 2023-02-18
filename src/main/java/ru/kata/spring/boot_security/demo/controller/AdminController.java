package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleRepository roleRepository;
    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {

        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    @GetMapping("")
    public String allUsers(Model model, @ModelAttribute("newUser") User newUser){
        model.addAttribute("users", userService.showUsers());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);

        List<Role> roles = (List<Role>) roleRepository.findAll();
        model.addAttribute("roles", roles);

        return "indexAdmin";
    }
    @PostMapping("/new")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/editUser/{id}")
    public String updateUser(@RequestParam("nameEdit") String name,
                             @RequestParam("sernameEdit") String sername,
                             @RequestParam("ageEdit") int age,
                             @RequestParam("usernameEdit") String username,
                             @RequestParam("passwordEdit") String password,
                             @RequestParam("rolesEdit") List<Role> roles,
                             @PathVariable("id") int id) {
        User user = new User();
        user.setName(name);
        user.setSername(sername);
        user.setAge(age);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(roles);
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser/{id}")
    public String delUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
