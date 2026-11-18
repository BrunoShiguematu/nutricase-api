package br.edu.unifio.nutricase.dtos;

import jakarta.validation.constraints.*;

public record ItemRefeicaoEntrada(
        @NotNull(message = "A refeição é obrigatória.") Long refeicaoId,
        @NotNull(message = "O alimento é obrigatório.") Long alimentoId,
        @NotNull(message = "A quantidade é obrigatória.") @Positive(message = "A quantidade deve ser maior que zero.") @Max(value = 2000, message = "A quantidade máxima é 2000 g.") Double quantidadeGramas) {
}
