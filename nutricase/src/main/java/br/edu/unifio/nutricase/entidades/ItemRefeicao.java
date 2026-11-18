package br.edu.unifio.nutricase.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ItemRefeicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Refeicao refeicao;

    @ManyToOne(optional = false)
    private Alimento alimento;

    @Column(nullable = false)
    private Double quantidadeGramas;
}
