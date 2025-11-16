package com.sistema.desafios.config;

import com.sistema.desafios.repository.UsuarioRepository;
import com.sistema.desafios.service.ConviteDesafioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ConviteDesafioService conviteDesafioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                                  Object handler, ModelAndView modelAndView) {
                if (modelAndView != null) {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                        try {
                            Long usuarioId = usuarioRepository.findByEmail(auth.getName())
                                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"))
                                    .getId();
                            
                            // Adicionar contador de convites pendentes para o navbar
                            long conviteCount = conviteDesafioService.listarRecebidosPendentes(usuarioId).size();
                            modelAndView.addObject("conviteCount", conviteCount);
                        } catch (Exception e) {
                            modelAndView.addObject("conviteCount", 0);
                        }
                    } else {
                        modelAndView.addObject("conviteCount", 0);
                    }
                }
            }
        });
    }
}

