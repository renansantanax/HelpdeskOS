package com.renan.osservice.application.controllers.chamados;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renan.osservice.domain.dtos.chamado.request.CriarMensagemChamadoRequestDto;
import com.renan.osservice.domain.dtos.chamado.response.MensagemChamadoResponseDto;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.services.chamados.MensagemChamadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chamados/mensagens/{id}")
@RequiredArgsConstructor
public class MensagemChamadoController {

    private final MensagemChamadoService mensagemChamadoService;

    @GetMapping("listar")
    public ResponseEntity<List<MensagemChamadoResponseDto>> listarMensagens(@PathVariable Long id) {
        List<MensagemChamadoResponseDto> mensagens = mensagemChamadoService.listarMensagensPorChamado(id);
        return ResponseEntity.ok(mensagens);
    }

    @PostMapping("enviar")
    public ResponseEntity<MensagemChamadoResponseDto> enviarMensagem(
        @PathVariable Long id,
        @RequestBody @Valid CriarMensagemChamadoRequestDto request,
        Authentication authentication
    ) {
        Usuario autor = (Usuario) authentication.getPrincipal();

        MensagemChamadoResponseDto resposta = mensagemChamadoService.adicionarMensagem(id, request, autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
}
