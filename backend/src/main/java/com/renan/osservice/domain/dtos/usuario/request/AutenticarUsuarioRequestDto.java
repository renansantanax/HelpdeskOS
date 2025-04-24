package com.renan.osservice.domain.dtos.usuario.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AutenticarUsuarioRequestDto {

	@Email(message = "Por favor, informe um endereço de email válido.")
	@NotEmpty(message = "Por favor, informe o email de acesso.")
	private String email;
	
	@NotEmpty(message = "Por favor, informe a senha de acesso.")
	private String senha;
	
}
