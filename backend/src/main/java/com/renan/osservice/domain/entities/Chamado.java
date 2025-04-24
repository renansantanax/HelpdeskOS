package com.renan.osservice.domain.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.renan.osservice.domain.enums.StatusChamado;
import com.renan.osservice.domain.enums.TipoChamado;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chamados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chamado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "titulo", length = 100, nullable = false)
	private String titulo;

	@Column(length = 1000, nullable = false)
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_chamado")
	private TipoChamado tipoChamado;
	
	private LocalDateTime dataAbertura;
	private LocalDateTime dataFechamento;

	@Enumerated(EnumType.STRING)
	private StatusChamado status;

	@ManyToOne
	@JoinColumn(name = "solicitante_id", nullable = false)
	private Usuario solicitante;

	@ManyToOne
	@JoinColumn(name = "responsavel_id")
	private Usuario responsavel;
	
	@Column(nullable = false)
	@Builder.Default
	private boolean encerrado = false;
	
	@OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL)
	private List<MensagemChamado> mensagens;
	
	@Builder.Default
	@OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Anexo> anexos = new ArrayList<>();

	
}
