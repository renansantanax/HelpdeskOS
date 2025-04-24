package com.renan.osservice.application.controllers.chamados;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renan.osservice.domain.dtos.chamado.response.ListarChamadosResponseDto;
import com.renan.osservice.domain.dtos.chamado.response.StatusQtdChamadosDto;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.services.chamados.ChamadoService;
import com.renan.osservice.domain.services.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/chamados/dashboard")
@Tag(name = "Chamados/Dashboard", description = "Endpoints para gerenciamento de lista de chamados")
public class DashboardChamadoController {

	@Autowired
	private ChamadoService chamadoService;

	@Autowired
	private UsuarioService usuarioService;

	@Operation(summary = "Listar chamados conforme o perfil do usuário autenticado")
	@ApiResponse(responseCode = "200", description = "Lista de chamados retornada com sucesso")
	@GetMapping("/listar-por-perfil")
	public ResponseEntity<List<ListarChamadosResponseDto>> listarChamados(Authentication auth) {
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		var chamados = chamadoService.listarChamadosPorPerfil(usuario);
		return ResponseEntity.ok(chamados);
	}

	@Operation(summary = "Dashboard geral de chamados agrupados por status")
	@GetMapping("/dashboard-geral")
	public ResponseEntity<List<StatusQtdChamadosDto>> listarPorStatus() {
		var chamados = chamadoService.chamadosPorStatus();
		return ResponseEntity.ok(chamados);
	}

	@Operation(summary = "Dashboard por usuário - chamados por status")
	@GetMapping("/dashboard-usuario/{id}")
	public ResponseEntity<List<StatusQtdChamadosDto>> listarPorStatusUsuario(
			@Parameter(description = "ID do usuário") @PathVariable UUID id) {
		var chamados = chamadoService.chamadoPorStatusUsuario(id);
		return ResponseEntity.ok(chamados);
	}

}
