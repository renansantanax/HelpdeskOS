package com.renan.osservice.domain.dtos.anexo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnexoResponseDto {
    private String id;

    private String nomeOriginal;
    private String url;
}