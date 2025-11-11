package com.sistema.desafios.controller;

import com.sistema.desafios.model.Desafio;
import com.sistema.desafios.model.Progresso;
import com.sistema.desafios.model.TipoAcaoProgresso;
import com.sistema.desafios.repository.UsuarioRepository;
import com.sistema.desafios.service.DesafioService;
import com.sistema.desafios.service.ProgressoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/desafios")
public class ProgressoController {

    @Autowired
    private ProgressoService progressoService;

    @Autowired
    private DesafioService desafioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Long idUsuarioAtual(Authentication auth) {
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"))
                .getId();
    }

    @GetMapping("/{id}/progresso")
    public String listarProgresso(@PathVariable Long id, Model model, Authentication auth) {
        List<Progresso> progresso = progressoService.listarDoDesafio(id);
        List<Progresso> meuProgresso = progressoService.listarDoUsuarioNoDesafio(idUsuarioAtual(auth), id);
        Desafio desafio = desafioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Desafio não encontrado"));

        model.addAttribute("progresso", progresso);
        model.addAttribute("meuProgresso", meuProgresso);
        model.addAttribute("desafio", desafio);
        return "progresso/lista";
    }

    @PostMapping("/{id}/progresso")
    public String registrarProgresso(@PathVariable Long id, @RequestParam TipoAcaoProgresso acao, @RequestParam(required = false) String nota, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        progressoService.registrar(usuarioId, id, acao, nota);
        return "redirect:/desafios/" + id + "/progresso"; // Redirecionar para a lista de progresso
    }
}
