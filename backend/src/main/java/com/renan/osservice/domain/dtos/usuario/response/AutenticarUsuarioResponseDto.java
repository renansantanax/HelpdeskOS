package com.renan.osservice.domain.dtos.usuario.response;

import java.util.List;
import java.util.UUID;

import com.renan.osservice.domain.enums.Perfil;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutenticarUsuarioResponseDto {

	private UUID id;
	private String nome;
	private String email;
	private List<Perfil> perfis;
	private String token;
	
}
