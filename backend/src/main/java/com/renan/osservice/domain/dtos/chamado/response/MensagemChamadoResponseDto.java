package com.renan.osservice.domain.dtos.chamado.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MensagemChamadoResponseDto {
    private Long id;
    private String conteudo;
    private String dataEnvio;
    private String autor;
}
