package com.renan.osservice.application.controllers.chamados;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.renan.osservice.domain.dtos.anexo.response.AnexoResponseDto;
import com.renan.osservice.domain.entities.Anexo;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.services.anexo.AnexoService;
import com.renan.osservice.domain.services.usuario.UsuarioService;

@RestController
@RequestMapping("/api/chamados/anexo")
public class AnexoChamadoController {

	@Autowired
	private AnexoService anexoService;

	@Autowired
	private UsuarioService usuarioService;
	
	
	private final Path raiz = Paths.get("uploads/chamados");

	@PostMapping("/upload-anexos/{id}")
	public ResponseEntity<?> uploadAnexos(@PathVariable Long id, @RequestParam("arquivos") List<MultipartFile> arquivos,
			Authentication auth) throws Exception {
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		anexoService.salvarAnexos(id, arquivos, usuario);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}/anexos")
	public ResponseEntity<List<AnexoResponseDto>> listarAnexos(@PathVariable Long id, Authentication auth)
			throws Exception {
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		List<AnexoResponseDto> anexos = anexoService.listarAnexosDoChamado(id, usuario);
		return ResponseEntity.ok(anexos);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable String id, Authentication auth) throws Exception {
	    Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
	    Anexo anexo = anexoService.buscarPorIdComPermissao(id, usuario);

	    Path path = Paths.get(raiz.toString(), anexo.getCaminho().split("/"));

	    if (!Files.exists(path)) {
	        throw new FileNotFoundException("Arquivo não encontrado no caminho especificado.");
	    }

	    Resource resource = new UrlResource(path.toUri());

	    return ResponseEntity.ok()
	        .contentType(MediaType.parseMediaType(anexo.getTipo()))
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + anexo.getNomeOriginal() + "\"")
	        .body(resource);
	}

}
