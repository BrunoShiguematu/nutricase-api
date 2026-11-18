package br.edu.unifio.nutricase.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.unifio.nutricase.entidades.Resolucao;
import br.edu.unifio.nutricase.entidades.StatusResolucao;

public interface ResolucaoRepositorio extends JpaRepository<Resolucao, Long> {
    /** Consulta JPQL: resoluções de um caso filtradas por status, mais recentes primeiro. */
    @Query("select r from Resolucao r where r.caso.id = :casoId and r.status = :status order by r.data desc, r.id desc")
    List<Resolucao> buscarPorCasoEStatus(@Param("casoId") Long casoId, @Param("status") StatusResolucao status);

    boolean existsByCasoId(Long casoId);
}
