package br.edu.unifio.nutricase.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.nutricase.entidades.ItemRefeicao;

public interface ItemRefeicaoRepositorio extends JpaRepository<ItemRefeicao, Long> {
    List<ItemRefeicao> findByRefeicaoId(Long refeicaoId);

    List<ItemRefeicao> findByRefeicaoResolucaoId(Long resolucaoId);

    boolean existsByAlimentoId(Long alimentoId);

    boolean existsByRefeicaoId(Long refeicaoId);
}
