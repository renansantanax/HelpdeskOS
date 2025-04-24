package com.renan.osservice.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renan.osservice.domain.entities.Anexo;

public interface AnexoRepository extends JpaRepository<Anexo, String> {
    List<Anexo> findByChamadoId(Long chamadoId);
  
    
}
