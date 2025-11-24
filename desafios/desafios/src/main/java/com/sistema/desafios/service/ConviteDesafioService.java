package com.sistema.desafios.service;

import com.sistema.desafios.model.ConviteDesafio;

import java.util.List;

public interface ConviteDesafioService {
    ConviteDesafio enviar(Long idRemetente, Long idDestinatario, Long idDesafio, String mensagem);
    void aceitar(Long idConvite, Long idUsuarioAtual);
    void recusar(Long idConvite, Long idUsuarioAtual);
    void cancelar(Long idConvite, Long idUsuarioAtual); // só remetente
    List<ConviteDesafio> listarRecebidosPendentes(Long idUsuario);
    List<ConviteDesafio> listarEnviadosPendentes(Long idUsuario);
    List<ConviteDesafio> listarAceitos(Long idUsuario);
}
