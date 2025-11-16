package com.sistema.desafios.controller;

import com.sistema.desafios.model.Desafio;
import com.sistema.desafios.model.DesafioFormDTO;
import com.sistema.desafios.model.Usuario;
import com.sistema.desafios.service.DesafioService;
import com.sistema.desafios.service.AmizadeService;
import com.sistema.desafios.repository.CategoriaRepository;
import com.sistema.desafios.repository.SubcategoriaRepository;
import com.sistema.desafios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/desafios")
public class DesafioController {

    @Autowired
    private DesafioService desafioService;

    @Autowired
    private AmizadeService amizadeService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    private Long idUsuarioAtual(Authentication auth) {
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"))
                .getId();
    }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         @RequestParam(required = false) String q,
                         @RequestParam(required = false) Long categoriaId,
                         Model model) {
        Page<Desafio> desafios = desafioService.listar(q, categoriaId, page, size);
        model.addAttribute("desafios", desafios);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "desafios/lista";
    }

    @GetMapping("/meus")
    public String meusDesafios(Model model, Authentication auth) {
        Long idCriador = idUsuarioAtual(auth);
        List<Desafio> meusDesafios = desafioService.listarMeusDesafios(idCriador);
        model.addAttribute("desafios", meusDesafios);
        model.addAttribute("meuId", idCriador);
        // Buscar amigos para o modal
        List<Usuario> amigos = amizadeService.listarMeusAmigos(idCriador);
        model.addAttribute("amigos", amigos);
        return "desafios/meus";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("desafioFormDTO", new DesafioFormDTO());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("subcategorias", subcategoriaRepository.findAll());
        return "desafios/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute DesafioFormDTO dto, Authentication auth) {
        Long idCriador = idUsuarioAtual(auth);
        desafioService.criar(dto, idCriador);
        return "redirect:/desafios";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        Desafio desafio = desafioService.detalhar(id);
        model.addAttribute("desafio", desafio);
        return "desafios/detalhe";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, Authentication auth) {
        Desafio desafio = desafioService.detalhar(id);
        if (!desafio.getCriador().getId().equals(idUsuarioAtual(auth))) {
            throw new SecurityException("Você não tem permissão para editar este desafio.");
        }
        
        // Converter Desafio para DesafioFormDTO
        DesafioFormDTO dto = new DesafioFormDTO();
        dto.setTitulo(desafio.getTitulo());
        dto.setDescricao(desafio.getDescricao());
        dto.setDataInicio(desafio.getDataInicio());
        dto.setDataFinal(desafio.getDataFinal());
        dto.setStatus(desafio.getStatus());
        dto.setPontuacaoMaxima(desafio.getPontuacaoMaxima());
        dto.setDificuldade(desafio.getDificuldade());
        dto.setCategoriaId(desafio.getCategoria().getId());
        if (desafio.getSubcategoria() != null) {
            dto.setSubcategoriaId(desafio.getSubcategoria().getId());
        }
        
        model.addAttribute("desafioFormDTO", dto);
        model.addAttribute("desafio", desafio);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("subcategorias", subcategoriaRepository.findAll());
        return "desafios/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute DesafioFormDTO dto, Authentication auth) {
        Long idCriador = idUsuarioAtual(auth);
        desafioService.atualizar(id, dto, idCriador);
        return "redirect:/desafios/" + id;
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, Authentication auth) {
        Long idCriador = idUsuarioAtual(auth);
        desafioService.excluir(id, idCriador);
        return "redirect:/desafios";
    }
}
