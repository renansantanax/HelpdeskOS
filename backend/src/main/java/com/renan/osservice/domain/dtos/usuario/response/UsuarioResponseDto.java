package com.renan.osservice.domain.dtos.usuario.response;

import java.util.List;
import java.util.UUID;

import com.renan.osservice.domain.enums.Perfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {
	private UUID id;
	private String nome;
	private String email;
	private List<Perfil> perfis;
}
