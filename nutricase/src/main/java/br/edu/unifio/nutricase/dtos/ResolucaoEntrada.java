package br.edu.unifio.nutricase.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record ResolucaoEntrada(
        @NotNull(message = "O caso é obrigatório.") Long casoId,
        @PastOrPresent(message = "A data não pode estar no futuro.") LocalDate data,
        @Size(max = 2000, message = "A justificativa pode ter no máximo 2000 caracteres.") String justificativa,
        @Positive(message = "A meta de kcal deve ser maior que zero.") Double metaKcal,
        @Positive(message = "A meta de carboidrato deve ser maior que zero.") Double metaCarboidrato,
        @Positive(message = "A meta de proteína deve ser maior que zero.") Double metaProteina,
        @Positive(message = "A meta de gordura deve ser maior que zero.") Double metaGordura) {
}
