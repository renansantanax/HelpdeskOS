package com.renan.osservice.infrastructure.components;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.renan.osservice.domain.entities.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenComponent {

	@Value("${jwt.secret}")
    private String secretKey;

    public String getToken(Usuario usuario) {

        // extrair perfis em formato de lista de strings
        var perfis = usuario.getPerfis()
                            .stream()
                            .map(Enum::name) // ADMIN, TECNICO, CLIENTE
                            .toList();

        return Jwts.builder()
                .setSubject(usuario.getEmail()) // email do usuário
                .claim("perfis", perfis)         // lista de perfis
                .setIssuedAt(new Date())         // data de geração
                .setExpiration(new Date(System.currentTimeMillis() + 7200000)) // expiração (2h)
                .signWith(SignatureAlgorithm.HS256, secretKey) // assinatura
                .compact();
    }
    
    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
}
