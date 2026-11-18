package br.edu.unifio.nutricase.dtos;

/** Comparação de um macronutriente: meta do aluno x total obtido na dieta. */
public record AvaliacaoMacro(String nutriente, double meta, double obtido, double desvioPercentual, boolean dentroDaTolerancia) {
}
