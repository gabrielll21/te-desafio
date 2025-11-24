package com.sistema.desafios.controller;

import com.sistema.desafios.dto.ProfileUpdateDTO;
import com.sistema.desafios.model.Usuario;
import com.sistema.desafios.repository.ProgressoRepository;
import com.sistema.desafios.service.AmizadeService;
import com.sistema.desafios.service.DesafioService;
import com.sistema.desafios.service.FileStorageService;
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
import java.io.IOException;
import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DesafioService desafioService;

    @Autowired
    private AmizadeService amizadeService;

    @Autowired
    private ProgressoRepository progressoRepository;

    @GetMapping("/perfil")
    public String getPerfil(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            
            // Calcular contadores
            Long usuarioId = usuario.getId();
            
            // Desafios criados
            long desafiosCriados = desafioService.listarMeusDesafios(usuarioId).size();
            model.addAttribute("desafiosCriados", desafiosCriados);
            
            // Desafios participando (desafios únicos onde o usuário tem progresso)
            Set<Long> desafiosComProgresso = progressoRepository.findAllByUsuarioId(usuarioId)
                    .stream()
                    .map(p -> p.getDesafio().getId())
                    .collect(Collectors.toSet());
            long desafiosParticipando = desafiosComProgresso.size();
            model.addAttribute("desafiosParticipando", desafiosParticipando);
            
            // Amigos
            long amigoCount = amizadeService.listarMeusAmigos(usuarioId).size();
            model.addAttribute("amigoCount", amigoCount);
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
            model.addAttribute("usuario", usuario);
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
                
                // Processar upload de foto de perfil
                if (profileUpdateDTO.getAvatar() != null && !profileUpdateDTO.getAvatar().isEmpty()) {
                    // Deletar foto antiga se existir
                    if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) {
                        fileStorageService.deletarFotoPerfil(usuario.getFotoPerfil());
                    }
                    
                    // Salvar nova foto
                    String fotoUrl = fileStorageService.salvarFotoPerfil(profileUpdateDTO.getAvatar(), usuario.getId());
                    if (fotoUrl != null) {
                        usuario.setFotoPerfil(fotoUrl);
                    }
                }
                
                usuarioService.atualizarUsuario(usuario);
                redirectAttributes.addFlashAttribute("message", "Perfil atualizado com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Usuário não encontrado.");
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao fazer upload da foto: " + e.getMessage());
            return "redirect:/perfil/editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar o perfil: " + e.getMessage());
            return "redirect:/perfil/editar";
        }
        
        return "redirect:/perfil";
    }
}
