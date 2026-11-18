package br.edu.unifio.nutricase.dtos;

import java.util.List;

import br.edu.unifio.nutricase.entidades.StatusResolucao;

public record AvaliacaoDieta(Long resolucaoId, StatusResolucao status, int toleranciaPercentual,
        List<AvaliacaoMacro> itens, int dentroDaTolerancia, int total, boolean dietaAdequada) {
}
