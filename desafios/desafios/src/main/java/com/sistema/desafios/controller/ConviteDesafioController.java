package com.sistema.desafios.controller;

import com.sistema.desafios.model.ConviteDesafio;
import com.sistema.desafios.repository.UsuarioRepository;
import com.sistema.desafios.service.ConviteDesafioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/convites")
public class ConviteDesafioController {

    @Autowired
    private ConviteDesafioService conviteDesafioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Long idUsuarioAtual(Authentication auth) {
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"))
                .getId();
    }

    @GetMapping("/recebidos")
    public String listarRecebidos(Model model, Authentication auth) {
        Long idUsuario = idUsuarioAtual(auth);
        List<ConviteDesafio> recebidos = conviteDesafioService.listarRecebidosPendentes(idUsuario);
        model.addAttribute("recebidos", recebidos);
        return "convites/recebidos"; // Nome da view
    }

    @GetMapping("/enviados")
    public String listarEnviados(Model model, Authentication auth) {
        Long idUsuario = idUsuarioAtual(auth);
        List<ConviteDesafio> enviados = conviteDesafioService.listarEnviadosPendentes(idUsuario);
        model.addAttribute("enviados", enviados);
        return "convites/enviados"; // Nome da view
    }

    @PostMapping("/enviar")
    public String enviarConvite(@RequestParam Long desafioId, @RequestParam Long toUserId, @RequestParam(required = false) String mensagem, Authentication auth) {
        Long idRemetente = idUsuarioAtual(auth);
        conviteDesafioService.enviar(idRemetente, toUserId, desafioId, mensagem);
        return "redirect:/convites/enviados";
    }

    @PostMapping("/{id}/aceitar")
    public String aceitarConvite(@PathVariable Long id, Authentication auth) {
        Long idUsuario = idUsuarioAtual(auth);
        conviteDesafioService.aceitar(id, idUsuario);
        return "redirect:/convites/recebidos";
    }

    @PostMapping("/{id}/recusar")
    public String recusarConvite(@PathVariable Long id, Authentication auth) {
        Long idUsuario = idUsuarioAtual(auth);
        conviteDesafioService.recusar(id, idUsuario);
        return "redirect:/convites/recebidos";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarConvite(@PathVariable Long id, Authentication auth) {
        Long idUsuario = idUsuarioAtual(auth);
        conviteDesafioService.cancelar(id, idUsuario);
        return "redirect:/convites/enviados";
    }
}
