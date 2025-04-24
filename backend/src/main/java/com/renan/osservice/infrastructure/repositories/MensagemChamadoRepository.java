package com.renan.osservice.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renan.osservice.domain.entities.MensagemChamado;

@Repository
public interface MensagemChamadoRepository extends JpaRepository<MensagemChamado, Long> {
	List<MensagemChamado> findByChamadoIdOrderByDataEnvioAsc(Long chamadoId);
}
