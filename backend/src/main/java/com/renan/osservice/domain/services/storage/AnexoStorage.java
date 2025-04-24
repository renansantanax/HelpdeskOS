package com.renan.osservice.domain.services.storage;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AnexoStorage {
    String salvar(String pastaRelativa, MultipartFile arquivo) throws IOException;
    Resource carregarComoRecurso(String caminho) throws IOException;
}
