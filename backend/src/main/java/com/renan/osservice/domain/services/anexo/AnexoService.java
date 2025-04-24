package com.renan.osservice.domain.services.anexo;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.renan.osservice.domain.dtos.anexo.response.AnexoResponseDto;
import com.renan.osservice.domain.entities.Anexo;
import com.renan.osservice.domain.entities.Chamado;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.mappers.ChamadoMapper;
import com.renan.osservice.domain.validators.ChamadoPermissaoValidator;
import com.renan.osservice.infrastructure.repositories.AnexoRepository;
import com.renan.osservice.infrastructure.repositories.ChamadoRepository;
import com.renan.osservice.infrastructure.repositories.UsuarioRepository;
import com.renan.osservice.infrastructure.storage.LocalFileStorageService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AnexoService {

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

	public void salvarAnexos(Long idChamado, List<MultipartFile> arquivos, Usuario usuario) throws Exception {
		Chamado chamado = buscarChamadoPorId(idChamado); // Retorna entidade, não DTO

		for (MultipartFile arquivo : arquivos) {
			String pastaRelativa = String.valueOf(chamado.getId());
			String caminhoRelativo = storageService.salvar(pastaRelativa, arquivo);

			Anexo anexo = new Anexo();
			anexo.setNomeOriginal(arquivo.getOriginalFilename());
			anexo.setTipo(arquivo.getContentType());
			anexo.setCaminho(caminhoRelativo);
			anexo.setChamado(chamado);

			anexoRepository.save(anexo);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public List<AnexoResponseDto> listarAnexosDoChamado(Long id, Usuario usuario) throws Exception {
		Chamado chamado = buscarChamadoPorId(id);

		// Validação: só permite o solicitante, responsável ou admin acessar
		if (!chamado.getSolicitante().getId().equals(usuario.getId())
				&& (chamado.getResponsavel() == null || !chamado.getResponsavel().getId().equals(usuario.getId()))
				&& !usuario.getPerfis().contains("ADMIN")) {
			throw new AccessDeniedException("Você não tem permissão para acessar os anexos deste chamado.");
		}

		return chamado.getAnexos().stream()
				.map(anexo -> new AnexoResponseDto(anexo.getId(), anexo.getNomeOriginal(), "/api/anexos/download/" + anexo.getId()))
				.toList();
	}


	@SuppressWarnings("unlikely-arg-type")
	public Anexo buscarPorIdComPermissao(String idAnexo, Usuario usuario) throws Exception {
		Anexo anexo = anexoRepository.findById(idAnexo)
				.orElseThrow(() -> new EntityNotFoundException("Anexo não encontrado"));

		Chamado chamado = anexo.getChamado();

		if (!chamado.getSolicitante().getId().equals(usuario.getId())
				&& (chamado.getResponsavel() == null || !chamado.getResponsavel().getId().equals(usuario.getId()))
				&& !usuario.getPerfis().contains("ADMIN")) {
			throw new AccessDeniedException("Você não tem permissão para acessar este anexo.");
		}

		return anexo;
	}

	private Chamado buscarChamadoPorId(Long id) {
		return chamadoRepository.buscarComAnexos(id)
				.orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));
	}
}
