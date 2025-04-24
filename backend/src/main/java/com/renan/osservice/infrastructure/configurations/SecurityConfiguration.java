package com.renan.osservice.infrastructure.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.renan.osservice.infrastructure.filters.JwtAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		return http
				  .cors(Customizer.withDefaults()) // mesmo efeito de .cors().and()
				  .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "auth/**",                 // se tiver endpoints de login/registro
	                "/api/auth/refresh",
	                "/api/auth/logout",
	                "/api/usuario/criar",       // libera o cadastro de usuário
	                "/api/usuario/autenticar",  // libera o login
	                "/v3/api-docs/**",
	                "/swagger-ui/**"            // libera swagger UI
	            ).permitAll()
	            
	            .requestMatchers("/api/usuario/listar").hasRole("ADMIN")
	            .requestMatchers("/api/chamados/consultar").hasRole("ADMIN")
	            
	            .anyRequest().authenticated()
	        )
	        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	        .build();
	}
}
