package br.edu.unifio.nutricase.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.nutricase.entidades.Alimento;

public interface AlimentoRepositorio extends JpaRepository<Alimento, Long> {
    /** Consulta derivada: busca de alimentos pelo nome, em ordem alfabética. */
    List<Alimento> findByNomeContainingIgnoreCaseOrderByNome(String nome);
}
