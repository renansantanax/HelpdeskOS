package com.renan.osservice.domain.dtos.usuario.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioAtualizarRequestDto {
	
	@Size(min = 3, max = 150, message = "Por favor, informe o nome do usuário com no mínimo 2 e no máximo 150 caracteres.")
	@NotEmpty(message = "Por favor, informe o nome do usuário.")
	private String nome;
	
	@Email(message = "Por favor, informe um endereço de email válido")
	@NotEmpty(message = "Por favor, informe o email do usuário.")
	private String email;
		
    @NotEmpty(message = "Por favor, informe ao menos um perfil para o usuário.")
    private Set<String> perfis;
	
}