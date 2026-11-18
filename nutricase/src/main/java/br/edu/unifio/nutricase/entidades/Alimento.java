package br.edu.unifio.nutricase.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** Valores nutricionais por 100 g do alimento. */
@Entity
@Getter
@Setter
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 60)
    private String grupo;

    private Double kcal;
    private Double carboidrato;
    private Double proteina;
    private Double gordura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigemAlimento origem;
}
