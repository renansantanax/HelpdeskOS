package com.renan.osservice.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.enums.Perfil;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{

	
	/*
	 * Consulta para retornar os dados de um usuário a partir do email
	 */
	@Query("""
	        select u from Usuario u
	        where u.email = :param1
	        """)
	Usuario findByEmail(@Param("param1") String email);
	
	
	/*
	 *  Consulta para retornar 1 (um) usuário a partir do email e senha
	 */
	@Query("""
            select u from Usuario u
            where u.email = :param1
            and u.senha = :param2
            """)
	Usuario findByEmailSenha(
			@Param("param1") String email, @Param("param2") String senha);
	
	
	List<Usuario> findAllByPerfisContaining(Perfil perfil);
	
}
