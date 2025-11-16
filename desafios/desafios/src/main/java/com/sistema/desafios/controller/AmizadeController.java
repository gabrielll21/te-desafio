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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/friends")
public class AmizadeController {

    @Autowired
    private AmizadeService amizadeService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar meus amigos
    @GetMapping
    public String listarAmigos(Model model, Authentication auth) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            List<Usuario> amigos = amizadeService.listarMeusAmigos(usuarioId);
            model.addAttribute("amigos", amigos);
            return "friends/list";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro ao carregar amigos: " + e.getMessage());
            model.addAttribute("amigos", List.of());
            return "friends/list";
        }
    }

    // Listar pedidos recebidos
    @GetMapping("/requests/incoming")
    public String listarPedidosRecebidos(Model model, Authentication auth) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            List<PedidoAmizade> pedidos = amizadeService.listarRecebidos(usuarioId);
            model.addAttribute("pedidosRecebidos", pedidos);
            return "friends/requests_incoming";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro ao carregar pedidos: " + e.getMessage());
            model.addAttribute("pedidosRecebidos", List.of());
            return "friends/requests_incoming";
        }
    }

    // Listar pedidos enviados
    @GetMapping("/requests/outgoing")
    public String listarPedidosEnviados(Model model, Authentication auth) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            List<PedidoAmizade> pedidos = amizadeService.listarEnviados(usuarioId);
            model.addAttribute("pedidosEnviados", pedidos);
            return "friends/requests_outgoing";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro ao carregar pedidos: " + e.getMessage());
            model.addAttribute("pedidosEnviados", List.of());
            return "friends/requests_outgoing";
        }
    }

    // Criar pedido de amizade
    @PostMapping("/requests")
    public String criarPedido(@RequestParam Long toUserId, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            amizadeService.criarPedido(usuarioId, toUserId);
            redirectAttributes.addFlashAttribute("success", "Pedido de amizade enviado com sucesso!");
            return "redirect:/friends/requests/outgoing";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/friends/perfil/" + toUserId;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erro ao enviar pedido: " + e.getMessage());
            return "redirect:/friends/perfil/" + toUserId;
        }
    }

    // Aceitar pedido
    @PostMapping("/requests/{id}/accept")
    public String aceitarPedido(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            amizadeService.aceitar(id, usuarioId);
            redirectAttributes.addFlashAttribute("success", "Pedido aceito! Agora vocês são amigos!");
            return "redirect:/friends/requests/incoming";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/friends/requests/incoming";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erro ao aceitar pedido: " + e.getMessage());
            return "redirect:/friends/requests/incoming";
        }
    }

    // Recusar pedido
    @PostMapping("/requests/{id}/decline")
    public String recusarPedido(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            amizadeService.recusar(id, usuarioId);
            redirectAttributes.addFlashAttribute("success", "Pedido recusado");
            return "redirect:/friends/requests/incoming";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/friends/requests/incoming";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erro ao recusar pedido: " + e.getMessage());
            return "redirect:/friends/requests/incoming";
        }
    }

    // Cancelar pedido
    @PostMapping("/requests/{id}/cancel")
    public String cancelarPedido(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            amizadeService.cancelar(id, usuarioId);
            redirectAttributes.addFlashAttribute("success", "Pedido cancelado");
            return "redirect:/friends/requests/outgoing";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/friends/requests/outgoing";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erro ao cancelar pedido: " + e.getMessage());
            return "redirect:/friends/requests/outgoing";
        }
    }

    // Buscar usuários
    @GetMapping("/buscar")
    public String buscarUsuarios(@RequestParam(required = false) String q, Model model, Authentication auth) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            List<Usuario> resultados = null;
            
            if (q != null && !q.trim().isEmpty()) {
                resultados = usuarioRepository.findByNomeContainingIgnoreCase(q.trim());
                if (resultados != null) {
                    resultados.removeIf(u -> u.getId().equals(usuarioId));
                }
            }
            
            model.addAttribute("resultados", resultados);
            model.addAttribute("query", q);
            model.addAttribute("meuId", usuarioId);
            return "friends/buscar";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro ao buscar usuários: " + e.getMessage());
            return "friends/buscar";
        }
    }

    // Ver perfil de outro usuário
    @GetMapping("/perfil/{id}")
    public String verPerfil(@PathVariable Long id, Model model, Authentication auth) {
        try {
            Long usuarioId = getIdUsuarioAtual(auth);
            
            // Não permitir ver o próprio perfil
            if (id.equals(usuarioId)) {
                return "redirect:/perfil";
            }
            
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            
            // Verificar status da relação
            boolean saoAmigos = amizadeService.saoAmigos(usuarioId, id);
            boolean haPendente = amizadeService.haPendenteEntre(usuarioId, id);
            
            // Verificar direção do pedido pendente
            boolean jaEnviouPedido = false;
            boolean recebeuPedido = false;
            
            if (haPendente) {
                List<PedidoAmizade> pedidosEnviados = amizadeService.listarEnviados(usuarioId);
                jaEnviouPedido = pedidosEnviados.stream()
                        .anyMatch(p -> p.getDestinatario() != null && p.getDestinatario().getId().equals(id));
                
                if (!jaEnviouPedido) {
                    List<PedidoAmizade> pedidosRecebidos = amizadeService.listarRecebidos(usuarioId);
                    recebeuPedido = pedidosRecebidos.stream()
                            .anyMatch(p -> p.getSolicitante() != null && p.getSolicitante().getId().equals(id));
                }
            }
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("saoAmigos", saoAmigos);
            model.addAttribute("jaEnviouPedido", jaEnviouPedido);
            model.addAttribute("recebeuPedido", recebeuPedido);
            model.addAttribute("meuId", usuarioId);
            
            return "friends/perfil";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/friends/buscar?error=" + e.getMessage();
        }
    }

    // API para buscar amigos (JSON)
    @GetMapping("/api/amigos")
    @ResponseBody
    public List<Usuario> listarAmigosApi(Authentication auth) {
        Long usuarioId = getIdUsuarioAtual(auth);
        return amizadeService.listarMeusAmigos(usuarioId);
    }

    // Helper para obter ID do usuário atual
    private Long getIdUsuarioAtual(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new IllegalStateException("Usuário não autenticado");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"))
                .getId();
    }
}
