package com.renan.osservice.domain.services.usuario;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.renan.osservice.domain.dtos.usuario.request.AutenticarUsuarioRequestDto;
import com.renan.osservice.domain.dtos.usuario.request.UsuarioAtualizarRequestDto;
import com.renan.osservice.domain.dtos.usuario.request.UsuarioRequestDto;
import com.renan.osservice.domain.dtos.usuario.response.AutenticarUsuarioResponseDto;
import com.renan.osservice.domain.dtos.usuario.response.UsuarioCriadoResponseDto;
import com.renan.osservice.domain.dtos.usuario.response.UsuarioResponseDto;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.enums.Perfil;
import com.renan.osservice.infrastructure.components.JwtTokenComponent;
import com.renan.osservice.infrastructure.configurations.PasswordEncoderConfiguration;
import com.renan.osservice.infrastructure.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	PasswordEncoderConfiguration passwordEncoderConfig;
	@Autowired
	JwtTokenComponent jwtTokenComponent;
	@Autowired
	ModelMapper modelMapper;

	public Usuario buscarPorEmail(String email) {

		var usuario = usuarioRepository.findByEmail(email);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}

		return usuario;

	}

	public UsuarioCriadoResponseDto criarUsuario(UsuarioRequestDto request) {

		var usuarioExistente = usuarioRepository.findByEmail(request.getEmail());

		if (usuarioExistente != null) {
			throw new IllegalArgumentException("Email já cadastrado.");
		}

		var usuario = Usuario.builder().nome(request.getNome()).email(request.getEmail())
				.senha(passwordEncoderConfig.passwordEncoder().encode(request.getSenha()))
				.perfis(request.getPerfis().stream().map(perfil -> Perfil.valueOf(perfil)).collect(Collectors.toSet()))
				.build();

		usuarioRepository.save(usuario);

		var response = UsuarioCriadoResponseDto.builder().id(usuario.getId()).nome(usuario.getNome())
				.email(usuario.getEmail()).perfis(usuario.getPerfis().stream().toList()).dataCriacao(Instant.now())
				.build();

		return response;

	}

	public AutenticarUsuarioResponseDto autenticarUsuario(AutenticarUsuarioRequestDto request) {

		// Buscar usuário pelo email
		var usuario = usuarioRepository.findByEmail(request.getEmail());

		if (usuario == null) {
			throw new IllegalArgumentException("Email ou senha incorretos.");
		}

		// Comparar senha digitada com a armazenada (hash)
		if (!passwordEncoderConfig.passwordEncoder().matches(request.getSenha(), usuario.getSenha())) {
			throw new IllegalArgumentException("Email ou senha incorretos.");
		}

		var response = AutenticarUsuarioResponseDto.builder().id(usuario.getId()).nome(usuario.getNome())
				.email(usuario.getEmail()).perfis(usuario.getPerfis().stream().toList())
				.token(jwtTokenComponent.getToken(usuario)).build();

		return response;

	}

	public UsuarioResponseDto atualizar(UUID id, UsuarioAtualizarRequestDto request) {

		var usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

		usuario.setNome(request.getNome());
		usuario.setEmail(request.getEmail());

		Set<Perfil> perfisConvertidos = request.getPerfis().stream().map(Perfil::valueOf).collect(Collectors.toSet());
		usuario.setPerfis(perfisConvertidos);

		usuarioRepository.save(usuario);

		return modelMapper.map(usuario, UsuarioResponseDto.class);
	}

	public UsuarioResponseDto excluir(UUID id) {

		var usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

		usuarioRepository.delete(usuario);

		return modelMapper.map(usuario, UsuarioResponseDto.class);

	}

	public List<UsuarioResponseDto> listar() {
		var usuarios = usuarioRepository.findAll();

		return usuarios.stream().map(usuario -> modelMapper.map(usuario, UsuarioResponseDto.class))
				.collect(Collectors.toList());

	}

}
