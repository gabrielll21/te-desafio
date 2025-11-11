package com.sistema.desafios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class MenuController {

    @GetMapping("/")
    public String showMenu(Model model) {
        model.addAttribute("message", "Bem-vindo ao Site de Desafios entre Amigos!");
        return "home";
    }
}
