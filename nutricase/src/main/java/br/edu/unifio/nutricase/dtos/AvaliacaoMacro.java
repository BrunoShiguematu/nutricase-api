package br.edu.unifio.nutricase.dtos;

public record AvaliacaoMacro(String nutriente, double meta, double obtido, double desvioPercentual, boolean dentroDaTolerancia) {
}
