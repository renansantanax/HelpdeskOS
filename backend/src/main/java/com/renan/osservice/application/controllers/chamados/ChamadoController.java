package com.renan.osservice.application.controllers.chamados;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renan.osservice.domain.dtos.chamado.request.AtualizarChamadoRequestDto;
import com.renan.osservice.domain.dtos.chamado.request.CriarChamadoRequestDto;
import com.renan.osservice.domain.dtos.chamado.response.ChamadoResponseDto;
import com.renan.osservice.domain.dtos.chamado.response.ListarChamadosResponseDto;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.services.chamados.ChamadoService;
import com.renan.osservice.domain.services.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chamados")
@Tag(name = "Chamados", description = "Endpoints para gerenciamento de chamados")
public class ChamadoController {

	@Autowired
	private ChamadoService chamadoService;

	@Autowired
	private UsuarioService usuarioService;

	@Operation(summary = "Criar um novo chamado", description = "Abre um novo chamado para o solicitante autenticado.")
	@ApiResponse(responseCode = "200", description = "Chamado criado com sucesso")
	@PostMapping("criar")
	public ResponseEntity<ChamadoResponseDto> criarChamado(@RequestBody @Valid CriarChamadoRequestDto request,
			Authentication auth) {
		Usuario solicitante = usuarioService.buscarPorEmail(auth.getName());
		var chamado = chamadoService.criarChamado(request, solicitante);
		return ResponseEntity.ok(chamado);
	}

	@Operation(summary = "Atualizar um chamado existente", description = "Atualiza os dados de um chamado pelo ID.")
	@ApiResponse(responseCode = "200", description = "Chamado atualizado com sucesso")
	@PutMapping("atualizar/{id}")
	public ResponseEntity<ChamadoResponseDto> atualizarChamado(
			@Parameter(description = "ID do chamado") @PathVariable Long id,
			@RequestBody @Valid AtualizarChamadoRequestDto request, Authentication auth) throws Exception {
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		var chamadoAtualizado = chamadoService.atualizarChamado(id, request, usuario);
		return ResponseEntity.ok(chamadoAtualizado);
	}

	@Operation(summary = "Encerrar um chamado", description = "Encerra um chamado pelo ID e marca como finalizado.")
	@ApiResponse(responseCode = "204", description = "Chamado encerrado com sucesso")
	@DeleteMapping("encerrar/{id}")
	public ResponseEntity<ChamadoResponseDto> encerrarChamado(
			@Parameter(description = "ID do chamado") @PathVariable Long id, Authentication auth) throws Exception {
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		chamadoService.encerrarChamado(id, usuario);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Obter detalhes de um chamado específico")
	@ApiResponse(responseCode = "200", description = "Chamado retornado com sucesso")
	@GetMapping("obter/{id}")
	public ResponseEntity<ChamadoResponseDto> obterChamado(
			@Parameter(description = "ID do chamado") @PathVariable Long id, Authentication auth) throws Exception {
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		var chamado = chamadoService.obterChamado(id, usuario);
		return ResponseEntity.ok(chamado);
	}

	@Operation(summary = "Listar todos os chamados (admin)")
	@GetMapping("consultar")
	public ResponseEntity<List<ListarChamadosResponseDto>> listarTodosChamados() {
		var chamados = chamadoService.listarTodosChamados();
		return ResponseEntity.ok(chamados);
	}

}
