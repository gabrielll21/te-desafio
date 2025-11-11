package com.sistema.desafios.service.impl;

import com.sistema.desafios.model.Historico;
import com.sistema.desafios.model.Progresso;
import com.sistema.desafios.model.TipoAcaoProgresso;
import com.sistema.desafios.repository.ProgressoRepository;
import com.sistema.desafios.repository.HistoricoRepository;
import com.sistema.desafios.repository.UsuarioRepository;
import com.sistema.desafios.repository.DesafioRepository;
import com.sistema.desafios.service.ProgressoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProgressoServiceImpl implements ProgressoService {

    @Autowired
    private ProgressoRepository progressoRepository;

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DesafioRepository desafioRepository;

    @Override
    public List<Progresso> listarDoDesafio(Long desafioId) {
        return progressoRepository.findAllByDesafioIdOrderByCriadoEmAsc(desafioId);
    }

    @Override
    public List<Progresso> listarDoUsuarioNoDesafio(Long usuarioId, Long desafioId) {
        return progressoRepository.findAllByUsuarioIdAndDesafioIdOrderByCriadoEmAsc(usuarioId, desafioId);
    }

    @Override
    @Transactional
    public Progresso registrar(Long usuarioId, Long desafioId, TipoAcaoProgresso acao, String nota) {
        // Validar que o desafio e o usuário existem
        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        var desafio = desafioRepository.findById(desafioId)
                .orElseThrow(() -> new IllegalArgumentException("Desafio não encontrado"));
        
        // Criar e salvar Progresso
        Progresso progresso = new Progresso();
        progresso.setUsuario(usuario);
        progresso.setDesafio(desafio);
        progresso.setAcao(acao);
        progresso.setNota(nota);
        progresso = progressoRepository.save(progresso);
        
        // Criar e salvar Historico
        Historico historico = new Historico();
        historico.setUsuario(usuario);
        historico.setDesafio(desafio);
        historico.setAcao(acao.name());
        historico.setDescricao(nota != null ? nota : "Registro de progresso: " + acao.name());
        historicoRepository.save(historico);
        
        return progresso;
    }
}
