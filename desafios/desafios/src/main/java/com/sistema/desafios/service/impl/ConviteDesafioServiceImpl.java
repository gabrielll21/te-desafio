package com.sistema.desafios.service.impl;

import com.sistema.desafios.model.ConviteDesafio;
import com.sistema.desafios.model.StatusConviteDesafio;
import com.sistema.desafios.repository.AmizadeRepository;
import com.sistema.desafios.repository.ConviteDesafioRepository;
import com.sistema.desafios.repository.DesafioRepository;
import com.sistema.desafios.repository.UsuarioRepository;
import com.sistema.desafios.service.ConviteDesafioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ConviteDesafioServiceImpl implements ConviteDesafioService {

    @Autowired
    private ConviteDesafioRepository conviteDesafioRepository;

    @Autowired
    private DesafioRepository desafioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AmizadeRepository amizadeRepository;

    @Override
    @Transactional
    public ConviteDesafio enviar(Long idRemetente, Long idDestinatario, Long idDesafio, String mensagem) {
        // Validar que remetente != destinatario
        if (idRemetente.equals(idDestinatario)) {
            throw new IllegalArgumentException("O remetente não pode ser igual ao destinatário.");
        }

        // Validar que o usuário remetente é amigo do destinatário
        // Verificar em ambas as direções (A-B ou B-A)
        boolean saoAmigos = amizadeRepository.findByUsuarioAIdAndUsuarioBId(idRemetente, idDestinatario).isPresent() ||
                           amizadeRepository.findByUsuarioAIdAndUsuarioBId(idDestinatario, idRemetente).isPresent();
        if (!saoAmigos) {
            throw new IllegalArgumentException("O remetente não é amigo do destinatário.");
        }

        // Validar que o desafio existe
        if (!desafioRepository.existsById(idDesafio)) {
            throw new IllegalArgumentException("Desafio não encontrado.");
        }

        // Impedir duplicidade se existir convite PENDENTE
        if (conviteDesafioRepository.findFirstByDesafioIdAndDestinatarioIdAndStatus(idDesafio, idDestinatario, StatusConviteDesafio.PENDENTE).isPresent()) {
            throw new IllegalArgumentException("Já existe um convite pendente para este desafio.");
        }

        // Criar e salvar o convite
        ConviteDesafio convite = new ConviteDesafio();
        convite.setRemetente(usuarioRepository.findById(idRemetente).orElseThrow());
        convite.setDestinatario(usuarioRepository.findById(idDestinatario).orElseThrow());
        convite.setDesafio(desafioRepository.findById(idDesafio).orElseThrow());
        convite.setMensagem(mensagem);
        convite.setStatus(StatusConviteDesafio.PENDENTE);
        return conviteDesafioRepository.save(convite);
    }

    @Override
    @Transactional
    public void aceitar(Long idConvite, Long idUsuarioAtual) {
        ConviteDesafio convite = conviteDesafioRepository.findById(idConvite).orElseThrow();
        if (!convite.getDestinatario().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Apenas o destinatário pode aceitar o convite.");
        }
        convite.setStatus(StatusConviteDesafio.ACEITO);
        convite.setRespondidoEm(Instant.now());
        conviteDesafioRepository.save(convite);
    }

    @Override
    @Transactional
    public void recusar(Long idConvite, Long idUsuarioAtual) {
        ConviteDesafio convite = conviteDesafioRepository.findById(idConvite).orElseThrow();
        if (!convite.getDestinatario().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Apenas o destinatário pode recusar o convite.");
        }
        convite.setStatus(StatusConviteDesafio.RECUSADO);
        convite.setRespondidoEm(Instant.now());
        conviteDesafioRepository.save(convite);
    }

    @Override
    @Transactional
    public void cancelar(Long idConvite, Long idUsuarioAtual) {
        ConviteDesafio convite = conviteDesafioRepository.findById(idConvite).orElseThrow();
        if (!convite.getRemetente().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Apenas o remetente pode cancelar o convite.");
        }
        convite.setStatus(StatusConviteDesafio.CANCELADO);
        convite.setRespondidoEm(Instant.now());
        conviteDesafioRepository.save(convite);
    }

    @Override
    public List<ConviteDesafio> listarRecebidosPendentes(Long idUsuario) {
        return conviteDesafioRepository.findAllByDestinatarioIdAndStatus(idUsuario, StatusConviteDesafio.PENDENTE);
    }

    @Override
    public List<ConviteDesafio> listarEnviadosPendentes(Long idUsuario) {
        return conviteDesafioRepository.findAllByRemetenteIdAndStatus(idUsuario, StatusConviteDesafio.PENDENTE);
    }
}
