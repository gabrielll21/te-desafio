package com.sistema.desafios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class MenuController {

    @GetMapping("/")
    public String showMenu(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            // Assuming principal has a getNome() method
            try {
                String nome = (String) principal.getClass().getMethod("getNome").invoke(principal);
                model.addAttribute("nomeUsuario", nome);
            } catch (Exception e) {
                model.addAttribute("nomeUsuario", "Usuário");
            }
        } else {
            model.addAttribute("nomeUsuario", "Usuário");
        }
        return "home";
    }
}
