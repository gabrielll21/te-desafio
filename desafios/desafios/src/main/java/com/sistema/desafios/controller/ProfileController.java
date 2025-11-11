package com.sistema.desafios.controller;

import com.sistema.desafios.dto.ProfileUpdateDTO;
import com.sistema.desafios.model.Usuario;
import com.sistema.desafios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public String getPerfil(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        }
        return "perfil/view";
    }

    @GetMapping("/perfil/editar")
    public String getEditarPerfil(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        if (usuario != null) {
            ProfileUpdateDTO dto = new ProfileUpdateDTO();
            dto.setNome(usuario.getNome());
            model.addAttribute("profileUpdateDTO", dto);
        }
        return "perfil/editar";
    }

    @PostMapping("/perfil")
    public String postEditarPerfil(@Valid @ModelAttribute ProfileUpdateDTO profileUpdateDTO, BindingResult result, RedirectAttributes redirectAttributes, Principal principal) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar o perfil. Verifique os dados.");
            return "redirect:/perfil/editar";
        }
        
        try {
            Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
            if (usuario != null) {
                usuario.setNome(profileUpdateDTO.getNome());
                // TODO: Implementar upload de avatar se necessário
                usuarioService.atualizarUsuario(usuario);
                redirectAttributes.addFlashAttribute("message", "Perfil atualizado com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Usuário não encontrado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar o perfil: " + e.getMessage());
            return "redirect:/perfil/editar";
        }
        
        return "redirect:/perfil";
    }
}
