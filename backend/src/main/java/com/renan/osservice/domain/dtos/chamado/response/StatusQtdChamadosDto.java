package com.renan.osservice.domain.dtos.chamado.response;

import com.renan.osservice.domain.enums.StatusChamado;

import lombok.Data;

@Data
public class StatusQtdChamadosDto {

	private StatusChamado status;
	private Long qtdChamados;
	
    public StatusQtdChamadosDto(StatusChamado status, Long qtdChamados) {
        this.status = status;
        this.qtdChamados = qtdChamados;
    }
	
}
