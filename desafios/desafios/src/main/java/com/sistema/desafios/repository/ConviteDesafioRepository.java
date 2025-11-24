package com.sistema.desafios.repository;

import com.sistema.desafios.model.ConviteDesafio;
import com.sistema.desafios.model.StatusConviteDesafio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConviteDesafioRepository extends JpaRepository<ConviteDesafio, Long> {
    Optional<ConviteDesafio> findFirstByDesafioIdAndDestinatarioIdAndStatus(Long desafioId, Long destinatarioId, StatusConviteDesafio status);
    
    @Query("SELECT c FROM ConviteDesafio c JOIN FETCH c.remetente JOIN FETCH c.destinatario JOIN FETCH c.desafio WHERE c.destinatario.id = :destinatarioId AND c.status = :status ORDER BY c.criadoEm DESC")
    List<ConviteDesafio> findAllByDestinatarioIdAndStatus(@Param("destinatarioId") Long destinatarioId, @Param("status") StatusConviteDesafio status);
    
    @Query("SELECT c FROM ConviteDesafio c JOIN FETCH c.remetente JOIN FETCH c.destinatario JOIN FETCH c.desafio WHERE c.remetente.id = :remetenteId AND c.status = :status ORDER BY c.criadoEm DESC")
    List<ConviteDesafio> findAllByRemetenteIdAndStatus(@Param("remetenteId") Long remetenteId, @Param("status") StatusConviteDesafio status);
    
    List<ConviteDesafio> findAllByDestinatarioId(Long destinatarioId);
    List<ConviteDesafio> findAllByRemetenteId(Long remetenteId);
    
    @Query("SELECT c FROM ConviteDesafio c JOIN FETCH c.remetente JOIN FETCH c.destinatario JOIN FETCH c.desafio WHERE c.destinatario.id = :destinatarioId AND c.status = :status ORDER BY c.criadoEm DESC")
    List<ConviteDesafio> findAllByDestinatarioIdAndStatusOrderByCriadoEmDesc(@Param("destinatarioId") Long destinatarioId, @Param("status") StatusConviteDesafio status);
}
