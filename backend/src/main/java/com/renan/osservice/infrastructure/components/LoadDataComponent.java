package com.renan.osservice.infrastructure.components;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.enums.Perfil;
import com.renan.osservice.infrastructure.configurations.PasswordEncoderConfiguration;
import com.renan.osservice.infrastructure.repositories.UsuarioRepository;

@Component
public class LoadDataComponent implements ApplicationRunner {

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	PasswordEncoderConfiguration passwordEncoderConfig;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		createUser("admin@email.com", "Admin", "Senha123@!", Set.of(Perfil.ADMIN));
		createUser("tecnico@email.com", "Técnico", "Senha123@!", Set.of(Perfil.TECNICO));
		createUser("cliente@email.com", "Cliente", "Senha123@!", Set.of(Perfil.CLIENTE));
			
			}
	
	private void createUser(String email, String nome, String senha, Set<Perfil> perfis) {
	    var usuarioExistente = usuarioRepository.findByEmail(email);

	    if (usuarioExistente != null) {
	        System.out.println("Usuário com email " + email + " já existe. Pulando criação.");
	        return;
	    }

	    var usuario = new Usuario();
	    usuario.setNome(nome);
	    usuario.setEmail(email);
	    usuario.setSenha(passwordEncoderConfig.passwordEncoder().encode(senha));
	    usuario.setPerfis(perfis);
	    usuarioRepository.save(usuario);
	}

}