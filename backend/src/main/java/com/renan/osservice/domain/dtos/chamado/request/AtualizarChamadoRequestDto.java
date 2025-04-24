package com.renan.osservice.domain.dtos.chamado.request;

import com.renan.osservice.domain.enums.StatusChamado;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AtualizarChamadoRequestDto {

	private String titulo;
	private String descricao;
	private StatusChamado status;
	
}
