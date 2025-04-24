package com.renan.osservice.domain.dtos.notificacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoChamadoDto {
    private Long chamadoId;
    private String titulo;
    private String descricao;
    private String tipo;
    private String mensagem;
    private String tecnicoNome;
    private String tecnicoEmail;
}