package com.renan.osservice.infrastructure.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.renan.osservice.domain.services.storage.AnexoStorage;

@Service
public class LocalFileStorageService implements AnexoStorage {

	  private final Path raiz = Paths.get("uploads/chamados");

	    @Override
	    public String salvar(String pastaRelativa, MultipartFile arquivo) throws IOException {
	        Path destino = raiz.resolve(pastaRelativa);
	        Files.createDirectories(destino);

	        Path caminhoFinal = destino.resolve(arquivo.getOriginalFilename());
	        arquivo.transferTo(caminhoFinal);

	        // Retorna o caminho relativo salvo
	        return raiz.relativize(caminhoFinal).toString().replace("\\", "/");
	    }

	    @Override
	    public Resource carregarComoRecurso(String caminhoRelativo) throws IOException {
	        Path caminhoAbsoluto = raiz.resolve(caminhoRelativo).normalize();
	        Resource resource = new UrlResource(caminhoAbsoluto.toUri());

	        if (resource.exists() && resource.isReadable()) {
	            return resource;
	        } else {
	            throw new IOException("Arquivo não pode ser lido: " + caminhoRelativo);
	        }
	    }

}
