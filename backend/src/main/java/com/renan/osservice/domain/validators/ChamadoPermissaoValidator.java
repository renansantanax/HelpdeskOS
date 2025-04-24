package com.renan.osservice.domain.validators;

import org.springframework.stereotype.Component;

import com.renan.osservice.domain.entities.Chamado;
import com.renan.osservice.domain.entities.Usuario;

@Component
public class ChamadoPermissaoValidator {

    public boolean podeEditar(Chamado chamado, Usuario usuario) {
        return isSolicitante(chamado, usuario) || ehResponsavel(chamado, usuario);
    }

    public boolean podeVisualizar(Chamado chamado, Usuario usuario) {
        return podeEditar(chamado, usuario); 
    }

    public boolean ehResponsavel(Chamado chamado, Usuario usuario) {
        return chamado.getResponsavel() != null &&
               chamado.getResponsavel().getId().equals(usuario.getId());
    }

    public boolean isSolicitante(Chamado chamado, Usuario usuario) {
        return chamado.getSolicitante().getId().equals(usuario.getId());
    }
}
