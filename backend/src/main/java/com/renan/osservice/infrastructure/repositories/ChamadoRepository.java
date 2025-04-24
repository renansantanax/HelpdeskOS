package com.renan.osservice.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.renan.osservice.domain.dtos.chamado.response.StatusQtdChamadosDto;
import com.renan.osservice.domain.entities.Chamado;
import com.renan.osservice.domain.entities.Usuario;
import com.renan.osservice.domain.enums.StatusChamado;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

	// Cliente: buscar chamados abertos do próprio usuário
	@Query("""
			    SELECT c FROM Chamado c
			    WHERE c.solicitante = :usuario
			    AND c.encerrado = false
			    ORDER BY c.dataAbertura DESC
			""")
	List<Chamado> listarChamadosDoSolicitante(@Param("usuario") Usuario usuario);

	// Técnico: buscar chamados atribuídos a ele
	@Query("""
			    SELECT c FROM Chamado c
			    WHERE c.responsavel = :usuario
			    AND c.encerrado = false
			    ORDER BY c.dataAbertura DESC
			""")
	List<Chamado> listarChamadosDoResponsavel(@Param("usuario") Usuario usuario);

	// Admin: todos os chamados não encerrados
	@Query("""
			    SELECT c FROM Chamado c
			    ORDER BY c.dataAbertura DESC
			""")
	List<Chamado> listarChamados();

	@Query("SELECT new com.renan.osservice.domain.dtos.chamado.response.StatusQtdChamadosDto(c.status, COUNT(c.id)) FROM Chamado c GROUP BY c.status")
	List<StatusQtdChamadosDto> contarChamadosPorStatus();

	@Query("SELECT new com.renan.osservice.domain.dtos.chamado.response.StatusQtdChamadosDto(c.status, COUNT(c.id)) FROM Chamado c WHERE c.solicitante.id = :usuarioId GROUP BY c.status")
	List<StatusQtdChamadosDto> contarChamadosPorStatusUsuario(@Param("usuarioId") UUID usuarioId);
	
    @Query("SELECT c FROM Chamado c LEFT JOIN FETCH c.anexos WHERE c.id = :id")
    Optional<Chamado> buscarComAnexos(@Param("id") Long id);
    
    int countByResponsavelAndStatusIn(Usuario responsavel, List<StatusChamado> status);

}
