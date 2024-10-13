package org.project.controllers;


import org.project.models.User;
import org.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/addNote")
    public String note(Model model) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // Добавление имени пользователя в модель
        model.addAttribute("username", currentUsername);
        model.addAttribute("addNote", "Заметки");
        return "addNote";
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/viewAllUsers")
    public String viewAllUsers(Model model) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isAdmin) {
            Iterable<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            return "viewAllUsers";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/admin/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/banUser/{id}")
    public String banUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setEnabled(false);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

}