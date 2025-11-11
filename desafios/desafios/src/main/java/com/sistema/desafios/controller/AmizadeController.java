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
        try {
            Long usuarioId = idUsuarioAtual(auth);
            List<PedidoAmizade> pedidosEnviados = amizadeService.listarEnviados(usuarioId);
            model.addAttribute("pedidosEnviados", pedidosEnviados);
            return "friends/requests/outgoing";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar pedidos enviados: " + e.getMessage());
            return "friends/requests/outgoing";
        }
    }

    @PostMapping("/requests")
    public String criarPedido(@RequestParam Long toUserId, Authentication auth) {
        try {
            Long usuarioId = idUsuarioAtual(auth);
            amizadeService.criarPedido(usuarioId, toUserId);
            return "redirect:/friends/requests/outgoing?success=pedido-enviado";
        } catch (IllegalArgumentException e) {
            return "redirect:/friends/perfil/" + toUserId + "?error=" + e.getMessage().replace(" ", "%20");
        } catch (Exception e) {
            return "redirect:/friends/perfil/" + toUserId + "?error=erro-ao-enviar-pedido";
        }
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

    @GetMapping("/buscar")
    public String buscarUsuarios(@RequestParam(required = false) String q, Model model, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        List<Usuario> resultados = null;
        
        if (q != null && !q.trim().isEmpty()) {
            resultados = usuarioRepository.findByNomeContainingIgnoreCase(q.trim());
            // Remover o próprio usuário dos resultados
            resultados.removeIf(u -> u.getId().equals(usuarioId));
        }
        
        model.addAttribute("resultados", resultados);
        model.addAttribute("query", q);
        model.addAttribute("meuId", usuarioId);
        return "friends/buscar";
    }

    @GetMapping("/perfil/{id}")
    public String verPerfil(@PathVariable Long id, Model model, Authentication auth) {
        Long usuarioId = idUsuarioAtual(auth);
        
        // Não permitir ver o próprio perfil
        if (id.equals(usuarioId)) {
            return "redirect:/perfil";
        }
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Verificar se já são amigos
        boolean saoAmigos = amizadeService.saoAmigos(usuarioId, id);
        
        // Verificar se já enviou pedido para este usuário (pendente)
        List<PedidoAmizade> pedidosEnviados = amizadeService.listarEnviados(usuarioId);
        boolean jaEnviouPedido = pedidosEnviados.stream()
                .anyMatch(p -> p.getDestinatario().getId().equals(id) && 
                              p.getStatus().name().equals("PENDENTE"));
        
        // Verificar se recebeu pedido deste usuário (pendente)
        List<PedidoAmizade> pedidosRecebidos = amizadeService.listarRecebidos(usuarioId);
        boolean recebeuPedido = pedidosRecebidos.stream()
                .anyMatch(p -> p.getSolicitante().getId().equals(id) && 
                              p.getStatus().name().equals("PENDENTE"));
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("saoAmigos", saoAmigos);
        model.addAttribute("jaEnviouPedido", jaEnviouPedido);
        model.addAttribute("recebeuPedido", recebeuPedido);
        model.addAttribute("meuId", usuarioId);
        
        return "friends/perfil";
    }

    private Long idUsuarioAtual(Authentication auth) {
        return usuarioRepository.findByEmail(auth.getName()).orElseThrow().getId();
    }
}
