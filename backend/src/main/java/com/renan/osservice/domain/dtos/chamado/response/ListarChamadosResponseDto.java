package com.renan.osservice.domain.dtos.chamado.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListarChamadosResponseDto {
	private Long id;
	private String titulo;
	private String descricao;
	private String status;
	private String tipoChamado;
	private String usuarioSolicitante;
	private String usuarioResponsavel;
	private LocalDateTime dataAbertura;
	private LocalDateTime dataFechamento;
}
