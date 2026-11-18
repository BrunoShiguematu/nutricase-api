package br.edu.unifio.nutricase.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.nutricase.entidades.Alimento;

public interface AlimentoRepositorio extends JpaRepository<Alimento, Long> {
    List<Alimento> findByNomeContainingIgnoreCaseOrderByNome(String nome);
}
