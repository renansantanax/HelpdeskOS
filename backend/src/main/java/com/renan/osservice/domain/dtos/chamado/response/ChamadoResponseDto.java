package com.renan.osservice.domain.dtos.chamado.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoResponseDto {
	private Long id;
	private String titulo;
	private String descricao;
	private String status;
	private String tipoChamado;
	private String usuarioSolicitante;
	private String usuarioResponsavel;
	private String dataAbertura;
	
	
	
}
