package br.edu.unifio.nutricase.dtos;

import java.util.List;

import br.edu.unifio.nutricase.entidades.StatusResolucao;

/** Relatório da avaliação da dieta de uma resolução. */
public record AvaliacaoDieta(Long resolucaoId, StatusResolucao status, int toleranciaPercentual,
        List<AvaliacaoMacro> itens, int dentroDaTolerancia, int total, boolean dietaAdequada) {
}
