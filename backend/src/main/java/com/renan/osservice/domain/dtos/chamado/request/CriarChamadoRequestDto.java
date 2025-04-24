package com.renan.osservice.domain.dtos.chamado.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriarChamadoRequestDto {

	@NotBlank(message = "Campo obrigatório")
	private String titulo;
	
	@NotBlank(message = "Campo obrigatório")
	private String descricao;
	
	@NotBlank(message = "Campo obrigatório")
	private String tipoChamado;
	
}
