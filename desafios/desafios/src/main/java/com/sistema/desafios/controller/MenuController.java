package com.sistema.desafios.controller;

import com.sistema.desafios.repository.UsuarioRepository;
import com.sistema.desafios.service.AmizadeService;
import com.sistema.desafios.service.ConviteDesafioService;
import com.sistema.desafios.service.DesafioService;
import com.sistema.desafios.repository.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @Autowired
    private DesafioService desafioService;

    @Autowired
    private AmizadeService amizadeService;

    @Autowired
    private ConviteDesafioService conviteDesafioService;

    @Autowired
    private ProgressoRepository progressoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String showMenu(Model model, Authentication auth) {
        model.addAttribute("message", "Bem-vindo ao Site de Desafios entre Amigos!");
        
        // Se o usuário estiver autenticado, calcular os contadores
        if (auth != null && auth.isAuthenticated()) {
            try {
                Long usuarioId = usuarioRepository.findByEmail(auth.getName())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"))
                        .getId();

                // Contar desafios criados pelo usuário
                long desafioCount = desafioService.listarMeusDesafios(usuarioId).size();
                model.addAttribute("desafioCount", desafioCount);

                // Contar amigos
                long amigoCount = amizadeService.listarMeusAmigos(usuarioId).size();
                model.addAttribute("amigoCount", amigoCount);

                // Contar convites recebidos pendentes
                long conviteCount = conviteDesafioService.listarRecebidosPendentes(usuarioId).size();
                model.addAttribute("conviteCount", conviteCount);

                // Contar progressos do usuário
                long progressoCount = progressoRepository.countByUsuarioId(usuarioId);
                model.addAttribute("progressoCount", progressoCount);
            } catch (Exception e) {
                // Em caso de erro, definir todos como 0
                model.addAttribute("desafioCount", 0);
                model.addAttribute("amigoCount", 0);
                model.addAttribute("conviteCount", 0);
                model.addAttribute("progressoCount", 0);
            }
        } else {
            // Se não estiver autenticado, definir todos como 0
            model.addAttribute("desafioCount", 0);
            model.addAttribute("amigoCount", 0);
            model.addAttribute("conviteCount", 0);
            model.addAttribute("progressoCount", 0);
        }
        
        return "home";
    }
}
