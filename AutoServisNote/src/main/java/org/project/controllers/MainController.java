package org.project.controllers;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

}