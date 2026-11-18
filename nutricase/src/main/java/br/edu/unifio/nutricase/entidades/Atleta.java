package br.edu.unifio.nutricase.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Atleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    private Integer idade;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    /** Peso em kg. */
    private Double peso;

    /** Altura em cm. */
    private Double altura;

    private Double percentualGordura;

    @Column(length = 200)
    private String objetivo;

    @Column(length = 100)
    private String fasePeriodizacao;
}
