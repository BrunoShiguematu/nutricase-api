package br.edu.unifio.nutricase.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.nutricase.entidades.Caso;
import br.edu.unifio.nutricase.entidades.Nivel;

public interface CasoRepositorio extends JpaRepository<Caso, Long> {
    /** Consulta derivada: casos de um nível cuja modalidade contém o texto informado. */
    List<Caso> findByNivelAndModalidadeContainingIgnoreCaseOrderByTitulo(Nivel nivel, String modalidade);

    boolean existsByAtletaId(Long atletaId);
}
