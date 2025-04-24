package com.renan.osservice.domain.services.chamados;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renan.osservice.domain.dtos.chamado.request.AtualizarChamadoRequestDto;
import com.renan.osservice.domain.dtos.chamado.request.CriarChamadoRequestDto;
import com.renan.osservice.domain.dtos.chamado.response.ChamadoResponseDto;
import com.renan.osservice.domain.dtos.chamado.response.ListarChamadosResponseDto;
import com.renan.osservice.domain.dtos.chamado.response.StatusQtdChamadosDto;
import com.renan.osservice.domain.dtos.notificacao.NotificacaoChamadoDto;
import com.renan.osservice.domain.entities.Chamado;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.enums.Perfil;
import com.renan.osservice.domain.enums.StatusChamado;
import com.renan.osservice.domain.enums.TipoChamado;
import com.renan.osservice.domain.mappers.ChamadoMapper;
import com.renan.osservice.domain.validators.ChamadoPermissaoValidator;
import com.renan.osservice.infrastructure.components.RabbitMQProducerComponent;
import com.renan.osservice.infrastructure.repositories.AnexoRepository;
import com.renan.osservice.infrastructure.repositories.ChamadoRepository;
import com.renan.osservice.infrastructure.repositories.UsuarioRepository;
import com.renan.osservice.infrastructure.storage.LocalFileStorageService;

@Service
public class ChamadoService {

	@Autowired
	ChamadoRepository chamadoRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	ChamadoMapper chamadoMapper;
	@Autowired
	ChamadoPermissaoValidator permissaoValidator;
	@Autowired
	LocalFileStorageService storageService;
	@Autowired
	AnexoRepository anexoRepository;
	@Autowired
	RabbitMQProducerComponent notificacaoProducer;

	public ChamadoResponseDto criarChamado(CriarChamadoRequestDto request, Usuario solicitante) {
		TipoChamado tipo = request.getTipoChamado() != null ? TipoChamado.valueOf(request.getTipoChamado()) : null;
		Chamado chamado = Chamado.builder().titulo(request.getTitulo()).descricao(request.getDescricao())
				.status(StatusChamado.ABERTO).tipoChamado(tipo).dataAbertura(LocalDateTime.now())
				.solicitante(solicitante).build();
		findTechWithLeastActiveCalls().ifPresent(chamado::setResponsavel);

		chamado = chamadoRepository.save(chamado);
		
		NotificacaoChamadoDto dto = new NotificacaoChamadoDto();
		dto.setChamadoId(chamado.getId());
		dto.setTitulo(chamado.getTitulo());
		dto.setDescricao(chamado.getDescricao());
		dto.setTipo(chamado.getTipoChamado().toString());
		
		
			notificacaoProducer.sendMessage(dto);
		
		return chamadoMapper.toCriarChamadoResponse(chamado);
	}

	public ChamadoResponseDto atualizarChamado(Long id, AtualizarChamadoRequestDto request, Usuario usuario)
			throws Exception {
		Chamado chamado = chamadoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Chamado não encontrado"));
		if (!permissaoValidator.podeEditar(chamado, usuario)) {
			throw new AccessDeniedException("Você não tem permissão.");
		}
		chamado.setTitulo(request.getTitulo());
		chamado.setDescricao(request.getDescricao());
		chamado.setStatus(request.getStatus());
		return chamadoMapper.toResponse(chamadoRepository.save(chamado));
	}

	public void encerrarChamado(Long id, Usuario usuario) {
		Chamado chamado = chamadoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Chamado não encontrado"));
		if (!permissaoValidator.ehResponsavel(chamado, usuario)) {
			throw new RuntimeException("Você não tem permissão para encerrar este chamado.");
		}
		chamado.setEncerrado(true);
		chamado.setDataFechamento(LocalDateTime.now());
		chamado.setStatus(StatusChamado.FINALIZADO);
		chamadoRepository.save(chamado);
	}

	public List<ListarChamadosResponseDto> listarChamadosPorPerfil(Usuario usuario) {
		List<Chamado> chamados = usuario.getPerfis().contains(Perfil.TECNICO)
				? chamadoRepository.listarChamadosDoResponsavel(usuario)
				: chamadoRepository.listarChamadosDoSolicitante(usuario);
		return chamadoMapper.toListarChamadosResponse(chamados);
	}

	public List<ListarChamadosResponseDto> listarTodosChamados() {
		return chamadoMapper.toListarChamadosResponse(chamadoRepository.listarChamados());
	}

	public ChamadoResponseDto obterChamado(Long id, Usuario usuario) throws Exception {
		Chamado chamado = chamadoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Chamado não encontrado"));
		if (!permissaoValidator.podeVisualizar(chamado, usuario)) {
			throw new AccessDeniedException("Você não tem permissão.");
		}
		return chamadoMapper.toResponse(chamado);
	}

	private Optional<Usuario> findTechWithLeastActiveCalls() {
	    List<Usuario> tecnicos = usuarioRepository.findAllByPerfisContaining(Perfil.TECNICO);

	    return tecnicos.stream()
	        .min(Comparator.comparingInt(tec -> chamadoRepository.countByResponsavelAndStatusIn(
	            tec, List.of(StatusChamado.ABERTO, StatusChamado.EM_ANDAMENTO)
	        )));
	}


	public List<StatusQtdChamadosDto> chamadosPorStatus() {
		return chamadoRepository.contarChamadosPorStatus();
	}

	public List<StatusQtdChamadosDto> chamadoPorStatusUsuario(UUID id) {
		return chamadoRepository.contarChamadosPorStatusUsuario(id);

	}

}
