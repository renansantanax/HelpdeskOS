package com.renan.osservice.domain.dtos.chamado.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CriarMensagemChamadoRequestDto {
    @NotBlank(message = "Mensagem não pode ser vazia")
    private String conteudo;
}
