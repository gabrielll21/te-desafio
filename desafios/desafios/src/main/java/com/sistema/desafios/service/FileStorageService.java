package com.sistema.desafios.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads/profiles/";
    
    private Path getUploadPath() throws IOException {
        // Tentar usar o diretório de recursos estáticos
        try {
            Path staticPath = Paths.get("src/main/resources/static/" + UPLOAD_DIR);
            if (!Files.exists(staticPath)) {
                Files.createDirectories(staticPath);
            }
            return staticPath;
        } catch (Exception e) {
            // Fallback: usar diretório temporário ou do projeto
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "profiles");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            return uploadPath;
        }
    }

    public String salvarFotoPerfil(MultipartFile file, Long usuarioId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Criar diretório se não existir
        Path uploadPath = getUploadPath();

        // Gerar nome único para o arquivo
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = "profile_" + usuarioId + "_" + UUID.randomUUID().toString() + extension;

        // Salvar arquivo
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Retornar URL relativa
        return "/" + UPLOAD_DIR + filename;
    }

    public void deletarFotoPerfil(String fotoUrl) {
        if (fotoUrl == null || fotoUrl.isEmpty()) {
            return;
        }

        try {
            // Remover barra inicial se existir
            String path = fotoUrl.startsWith("/") ? fotoUrl.substring(1) : fotoUrl;
            
            // Tentar diferentes caminhos possíveis
            Path[] possiblePaths = {
                Paths.get("src/main/resources/static/" + path),
                Paths.get(System.getProperty("user.dir"), path)
            };
            
            for (Path filePath : possiblePaths) {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    break;
                }
            }
        } catch (IOException e) {
            // Log do erro, mas não interromper o processo
            System.err.println("Erro ao deletar foto: " + e.getMessage());
        }
    }
}

