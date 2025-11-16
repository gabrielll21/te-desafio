package com.sistema.desafios.service.impl;

import com.sistema.desafios.model.*;
import com.sistema.desafios.repository.*;
import com.sistema.desafios.service.DesafioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DesafioServiceImpl implements DesafioService {

    @Autowired
    private DesafioRepository desafioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Optional<Desafio> findById(Long id) {
        return desafioRepository.findById(id);
    }

    @Override
    public List<Desafio> findAll() {
        return desafioRepository.findAll();
    }

    @Override
    public Page<Desafio> listar(String q, Long categoriaId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        if (q != null && !q.isEmpty() && categoriaId != null) {
            // Buscar por título e categoria
            return desafioRepository.findByTituloContainingIgnoreCaseAndCategoriaId(q, categoriaId, pageable);
        } else if (q != null && !q.isEmpty()) {
            return desafioRepository.findByTituloContainingIgnoreCase(q, pageable);
        } else if (categoriaId != null) {
            return desafioRepository.findByCategoriaId(categoriaId, pageable);
        } else {
            return desafioRepository.findAll(pageable);
        }
    }

    @Override
    public List<Desafio> listarMeusDesafios(Long idCriador) {
        return desafioRepository.findByCriadorId(idCriador);
    }

    @Override
    @Transactional
    public Desafio criar(DesafioFormDTO dto, Long idCriador) {
        Desafio desafio = new Desafio();
        desafio.setTitulo(dto.getTitulo());
        desafio.setDescricao(dto.getDescricao());
        desafio.setDataInicio(dto.getDataInicio());
        desafio.setDataFinal(dto.getDataFinal());
        desafio.setStatus(dto.getStatus());
        desafio.setPontuacaoMaxima(dto.getPontuacaoMaxima());
        desafio.setDificuldade(dto.getDificuldade());
        
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        desafio.setCategoria(categoria);
        
        if (dto.getSubcategoriaId() != null) {
            Subcategoria subcategoria = subcategoriaRepository.findById(dto.getSubcategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Subcategoria não encontrada"));
            desafio.setSubcategoria(subcategoria);
        }
        
        Usuario criador = usuarioRepository.findById(idCriador)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        desafio.setCriador(criador);
        
        return desafioRepository.save(desafio);
    }

    @Override
    @Transactional
    public Desafio atualizar(Long id, DesafioFormDTO dto, Long idCriador) {
        Desafio desafio = desafioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desafio não encontrado"));
        
        if (!desafio.getCriador().getId().equals(idCriador)) {
            throw new SecurityException("Você não tem permissão para editar este desafio.");
        }
        
        desafio.setTitulo(dto.getTitulo());
        desafio.setDescricao(dto.getDescricao());
        desafio.setDataInicio(dto.getDataInicio());
        desafio.setDataFinal(dto.getDataFinal());
        desafio.setStatus(dto.getStatus());
        desafio.setPontuacaoMaxima(dto.getPontuacaoMaxima());
        desafio.setDificuldade(dto.getDificuldade());
        
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        desafio.setCategoria(categoria);
        
        if (dto.getSubcategoriaId() != null) {
            Subcategoria subcategoria = subcategoriaRepository.findById(dto.getSubcategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Subcategoria não encontrada"));
            desafio.setSubcategoria(subcategoria);
        } else {
            desafio.setSubcategoria(null);
        }
        
        return desafioRepository.save(desafio);
    }

    @Override
    @Transactional
    public void excluir(Long id, Long idCriador) {
        Desafio desafio = desafioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desafio não encontrado"));
        
        if (!desafio.getCriador().getId().equals(idCriador)) {
            throw new SecurityException("Você não tem permissão para excluir este desafio.");
        }
        
        desafioRepository.delete(desafio);
    }

    @Override
    public Desafio detalhar(Long id) {
        return desafioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desafio não encontrado"));
    }
}
