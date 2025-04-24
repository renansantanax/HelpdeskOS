package com.renan.osservice.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.renan.osservice.domain.dtos.chamado.response.ChamadoResponseDto;
import com.renan.osservice.domain.dtos.chamado.response.ListarChamadosResponseDto;
import com.renan.osservice.domain.entities.Chamado;

@Component
public class ChamadoMapper {

    public ChamadoResponseDto toCriarChamadoResponse(Chamado chamado) {
        return ChamadoResponseDto.builder()
            .id(chamado.getId())
            .titulo(chamado.getTitulo())
            .descricao(chamado.getDescricao())
            .status(chamado.getStatus().toString())
            .tipoChamado(chamado.getTipoChamado() != null ? chamado.getTipoChamado().toString() : "Não informado")
            .dataAbertura(chamado.getDataAbertura().toString())
            .usuarioSolicitante(chamado.getSolicitante().getNome())
            .usuarioResponsavel(chamado.getResponsavel() != null ? chamado.getResponsavel().getNome() : "Não atribuído")
            .build();
    }

    public ChamadoResponseDto toResponse(Chamado chamado) {
        return ChamadoResponseDto.builder()
            .id(chamado.getId())
            .titulo(chamado.getTitulo())
            .descricao(chamado.getDescricao())
            .status(chamado.getStatus().toString())
            .tipoChamado(chamado.getTipoChamado() != null ? chamado.getTipoChamado().toString() : "Não informado")
            .dataAbertura(chamado.getDataAbertura().toString())
            .usuarioSolicitante(chamado.getSolicitante().getNome())
            .usuarioResponsavel(chamado.getResponsavel() != null ? chamado.getResponsavel().getNome() : "Não atribuído")
            .build();
    }

    public List<ListarChamadosResponseDto> toListarChamadosResponse(List<Chamado> chamados) {
        return chamados.stream()
            .map(this::toListarChamado)
            .collect(Collectors.toList());
    }

    private ListarChamadosResponseDto toListarChamado(Chamado chamado) {
        return ListarChamadosResponseDto.builder()
            .id(chamado.getId())
            .titulo(chamado.getTitulo())
            .descricao(chamado.getDescricao())
            .status(chamado.getStatus().name())
            .tipoChamado(chamado.getTipoChamado() != null ? chamado.getTipoChamado().toString() : "Não informado")
            .dataAbertura(chamado.getDataAbertura())
            .usuarioSolicitante(chamado.getSolicitante().getNome())
            .usuarioResponsavel(chamado.getResponsavel() != null ? chamado.getResponsavel().getNome() : "Não atribuído")
            .build();
    }
    
}
