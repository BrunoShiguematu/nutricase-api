package br.edu.unifio.nutricase.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.nutricase.entidades.Refeicao;

public interface RefeicaoRepositorio extends JpaRepository<Refeicao, Long> {
    List<Refeicao> findByResolucaoIdOrderByHorario(Long resolucaoId);
}
