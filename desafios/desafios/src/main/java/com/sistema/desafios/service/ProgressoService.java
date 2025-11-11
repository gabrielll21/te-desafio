package com.sistema.desafios.service;

import com.sistema.desafios.model.Progresso;
import com.sistema.desafios.model.TipoAcaoProgresso;
import java.util.List;

public interface ProgressoService {
    List<Progresso> listarDoDesafio(Long desafioId);
    List<Progresso> listarDoUsuarioNoDesafio(Long usuarioId, Long desafioId);
    Progresso registrar(Long usuarioId, Long desafioId, TipoAcaoProgresso acao, String nota);
}
