package com.sistema.desafios.service;

import com.sistema.desafios.model.PedidoAmizade;
import com.sistema.desafios.model.Usuario;
import java.util.List;

public interface AmizadeService {
    PedidoAmizade criarPedido(Long idSolicitante, Long idDestinatario);
    void aceitar(Long idPedido, Long idUsuarioAtual);
    void recusar(Long idPedido, Long idUsuarioAtual);
    void cancelar(Long idPedido, Long idUsuarioAtual);
    boolean saoAmigos(Long id1, Long id2);
    boolean haPendenteEntre(Long id1, Long id2);
    List<Usuario> listarMeusAmigos(Long meuId);
    List<PedidoAmizade> listarRecebidos(Long meuId);
    List<PedidoAmizade> listarEnviados(Long meuId);
}
