package com.renan.osservice.application.controllers.usuario;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renan.osservice.domain.dtos.usuario.request.AutenticarUsuarioRequestDto;
import com.renan.osservice.domain.dtos.usuario.request.UsuarioAtualizarRequestDto;
import com.renan.osservice.domain.dtos.usuario.request.UsuarioRequestDto;
import com.renan.osservice.domain.dtos.usuario.response.AutenticarUsuarioResponseDto;
import com.renan.osservice.domain.dtos.usuario.response.UsuarioCriadoResponseDto;
import com.renan.osservice.domain.dtos.usuario.response.UsuarioResponseDto;
import com.renan.osservice.domain.services.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	// Injeção de dependência
	@Autowired
	UsuarioService usuarioService;

	@Operation(summary = "Serviço da API para criar um novo usuário.")
	@PostMapping("criar")
	public ResponseEntity<UsuarioCriadoResponseDto> criar(@RequestBody @Valid UsuarioRequestDto request) {
		var response = usuarioService.criarUsuario(request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Serviço da API para autenticar um usuário.")
	@PostMapping("autenticar")
	public ResponseEntity<AutenticarUsuarioResponseDto> autenticar(
			@RequestBody @Valid AutenticarUsuarioRequestDto request) {
		var response = usuarioService.autenticarUsuario(request);
		return ResponseEntity.ok(response);
	}
	
	@Operation
	@PutMapping("atualizar/{id}")
	public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable UUID id, @RequestBody @Valid UsuarioAtualizarRequestDto request) throws Exception {
		return ResponseEntity.ok(usuarioService.atualizar(id, request));		
	}
	
	@Operation
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<UsuarioResponseDto> excluir(@PathVariable UUID id) throws Exception {
		return ResponseEntity.ok(usuarioService.excluir(id));		
	}
	
	@Operation(summary = "Retornar todos os usuários cadastrados")
	@GetMapping("listar")
	public ResponseEntity<List<UsuarioResponseDto>> listar() throws Exception {
		return ResponseEntity.ok(usuarioService.listar());	
	}
	

}