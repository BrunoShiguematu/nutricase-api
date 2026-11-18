package br.edu.unifio.nutricase.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Refeicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Resolucao resolucao;

    @Column(nullable = false, length = 60)
    private String nome;

    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    private MarcadorRefeicao marcador;
}
