package br.edu.unifio.nutricase.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Tentativa de um aluno de resolver um caso: metas calculadas + dieta montada. */
@Entity
@Getter
@Setter
public class Resolucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Caso caso;

    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusResolucao status;

    @Column(length = 2000)
    private String justificativa;

    /** Metas que o PRÓPRIO aluno calculou (a dieta é avaliada contra elas). */
    private Double metaKcal;
    private Double metaCarboidrato;
    private Double metaProteina;
    private Double metaGordura;

    private LocalDateTime submetidaEm;
}
