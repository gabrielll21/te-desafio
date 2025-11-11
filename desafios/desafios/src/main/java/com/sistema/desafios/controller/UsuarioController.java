package com.sistema.desafios.controller;

import com.sistema.desafios.model.Usuario;
import com.sistema.desafios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cadastro")
    public String mostrarCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@Valid Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "cadastro";
        }
        
        // Verifica se o email já existe
        if (usuarioService.buscarPorEmail(usuario.getEmail()) != null) {
            model.addAttribute("erro", "Este email já está cadastrado!");
            return "cadastro";
        }
        
        // Define role padrão se não foi informada
        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            usuario.setRole("USER");
        }
        usuarioService.salvarUsuario(usuario);
        return "redirect:/login?cadastro=sucesso";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    /*@GetMapping("/home")
    public String mostrarHome(Model model) {
        return "home";
    }*/

}
