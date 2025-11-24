package com.sistema.desafios.repository;

import com.sistema.desafios.model.ConviteDesafio;
import com.sistema.desafios.model.StatusConviteDesafio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConviteDesafioRepository extends JpaRepository<ConviteDesafio, Long> {
    Optional<ConviteDesafio> findFirstByDesafioIdAndDestinatarioIdAndStatus(Long desafioId, Long destinatarioId, StatusConviteDesafio status);
    List<ConviteDesafio> findAllByDestinatarioIdAndStatus(Long destinatarioId, StatusConviteDesafio status);
    List<ConviteDesafio> findAllByRemetenteIdAndStatus(Long remetenteId, StatusConviteDesafio status);
    List<ConviteDesafio> findAllByDestinatarioId(Long destinatarioId);
    List<ConviteDesafio> findAllByRemetenteId(Long remetenteId);
    List<ConviteDesafio> findAllByDestinatarioIdAndStatusOrderByCriadoEmDesc(Long destinatarioId, StatusConviteDesafio status);
}
