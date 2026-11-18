package br.edu.unifio.nutricase.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Caso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 80)
    private String modalidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nivel nivel;

    @Column(length = 2000)
    private String descricao;

    /** Tolerância (em %) aceita entre a dieta montada e as metas do aluno. */
    private Integer toleranciaDieta;

    @ManyToOne(optional = false)
    private Atleta atleta;
}
