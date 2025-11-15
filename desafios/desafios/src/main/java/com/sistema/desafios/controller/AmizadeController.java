package com.sistema.desafios.controller;

import com.sistema.desafios.model.PedidoAmizade;
import com.sistema.desafios.model.Usuario;
import com.sistema.desafios.repository.UsuarioRepository;
import com.sistema.desafios.service.AmizadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/friends")
public class AmizadeController {

    @Autowired
    private AmizadeService amizadeService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarAmigos(Model model, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        List<Usuario> amigos = amizadeService.listarMeusAmigos(usuarioId);
        model.addAttribute("amigos", amigos);
        return "friends/list";
    }

    @GetMapping("/requests/incoming")
    public String listarPedidosRecebidos(Model model, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        List<PedidoAmizade> pedidosRecebidos = amizadeService.listarRecebidos(usuarioId);
        model.addAttribute("pedidosRecebidos", pedidosRecebidos);
        return "friends/requests/incoming";
    }

    @GetMapping("/requests/outgoing")
    public String listarPedidosEnviados(Model model, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        List<PedidoAmizade> pedidosEnviados = amizadeService.listarEnviados(usuarioId);
        model.addAttribute("pedidosEnviados", pedidosEnviados);
        return "friends/requests/outgoing";
    }

    @PostMapping("/requests")
    public String criarPedido(@RequestParam Long toUserId, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        amizadeService.criarPedido(usuarioId, toUserId);
        return "redirect:/friends/requests/outgoing";
    }

    @PostMapping("/requests/{id}/accept")
    public String aceitarPedido(@PathVariable Long id, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        amizadeService.aceitar(id, usuarioId);
        return "redirect:/friends/requests/incoming";
    }

    @PostMapping("/requests/{id}/decline")
    public String recusarPedido(@PathVariable Long id, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        amizadeService.recusar(id, usuarioId);
        return "redirect:/friends/requests/incoming";
    }

    @PostMapping("/requests/{id}/cancel")
    public String cancelarPedido(@PathVariable Long id, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        amizadeService.cancelar(id, usuarioId);
        return "redirect:/friends/requests/outgoing";
    }

    private Long idUsuarioAtual(Authentication auth) {
        return usuarioRepository.findByEmail(auth.getName()).orElseThrow().getId();
    }
}
