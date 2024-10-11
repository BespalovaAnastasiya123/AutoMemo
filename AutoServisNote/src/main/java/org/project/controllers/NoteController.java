package org.project.controllers;

import org.project.models.Note;
import org.project.models.User;
import org.project.repo.NoteRepository;
import org.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;


@Controller
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/addNote")
    public String noteUser(Model model,
                           @RequestParam String date,
                           @RequestParam String carBrand,
                           @RequestParam String carModel,
                           @RequestParam String maintenanceWork,
                           @RequestParam Double maintenanceCost) {

        // Проверка на пустые поля
        if (date.isEmpty() || carBrand.isEmpty() || carModel.isEmpty() || maintenanceWork.isEmpty() || maintenanceCost == null) {
            model.addAttribute("message", "Все поля обязательны для заполнения!");
            return "addNote";
        }

        // Проверка формата даты
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            model.addAttribute("message", "Неверный формат даты. Используйте формат ГГГГ-ММ-ДД.");
            return "addNote";
        }

        // Проверка диапазона стоимости ТО
        if (maintenanceCost < 0 || maintenanceCost > 1000000) {
            model.addAttribute("message", "Стоимость ТО должна быть в диапазоне от 0 до 1,000,000.");
            return "addNote";
        }

        // Сохранение заметки
        Note note = new Note(date, carBrand, carModel, maintenanceWork, maintenanceCost);
        noteRepository.save(note);
        model.addAttribute("message1", "Заметка успешно добавлена!");
        return "addNote";
    }


    @GetMapping("/viewNote")
    public String viewNotes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername);

        List<Note> notes = noteRepository.findByUser(currentUser);
        model.addAttribute("note", notes);
        return "viewNote";
    }
}
