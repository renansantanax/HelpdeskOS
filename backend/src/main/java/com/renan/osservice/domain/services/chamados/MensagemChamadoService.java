package com.renan.osservice.domain.services.chamados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renan.osservice.domain.dtos.chamado.request.CriarMensagemChamadoRequestDto;
import com.renan.osservice.domain.dtos.chamado.response.MensagemChamadoResponseDto;
import com.renan.osservice.domain.entities.Chamado;
import com.renan.osservice.domain.entities.MensagemChamado;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.infrastructure.repositories.ChamadoRepository;
import com.renan.osservice.infrastructure.repositories.MensagemChamadoRepository;
import com.renan.osservice.infrastructure.repositories.UsuarioRepository;

@Service
public class MensagemChamadoService {

    @Autowired ChamadoRepository chamadoRepository;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired MensagemChamadoRepository mensagemChamadoRepository;
	
    public List<MensagemChamadoResponseDto> listarMensagensPorChamado(Long chamadoId) {
        List<MensagemChamado> mensagens = mensagemChamadoRepository.findByChamadoIdOrderByDataEnvioAsc(chamadoId);

        return mensagens.stream()
            .map(m -> MensagemChamadoResponseDto.builder()
                .id(m.getId())
                .conteudo(m.getConteudo())
                .dataEnvio(m.getDataEnvio().toString())
                .autor(m.getAutor().getNome())
                .build())
            .toList();
    }
    
    public MensagemChamadoResponseDto adicionarMensagem(Long chamadoId, CriarMensagemChamadoRequestDto request, Usuario autor) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
            .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        MensagemChamado mensagem = MensagemChamado.builder()
            .conteudo(request.getConteudo())
            .dataEnvio(LocalDateTime.now())
            .autor(autor)
            .chamado(chamado)
            .build();

        mensagem = mensagemChamadoRepository.save(mensagem);

        return MensagemChamadoResponseDto.builder()
            .id(mensagem.getId())
            .conteudo(mensagem.getConteudo())
            .dataEnvio(mensagem.getDataEnvio().toString())
            .autor(mensagem.getAutor().getNome())
            .build();
    }
    
}
