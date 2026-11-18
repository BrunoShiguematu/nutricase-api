package br.edu.unifio.nutricase.dtos;

import br.edu.unifio.nutricase.entidades.OrigemAlimento;
import jakarta.validation.constraints.*;

public record AlimentoEntrada(
        @NotBlank(message = "O nome é obrigatório.") @Size(max = 120, message = "O nome pode ter no máximo 120 caracteres.") String nome,
        @Size(max = 60, message = "O grupo pode ter no máximo 60 caracteres.") String grupo,
        @NotNull(message = "As kcal são obrigatórias.") @PositiveOrZero(message = "As kcal não podem ser negativas.") Double kcal,
        @NotNull(message = "O carboidrato é obrigatório.") @PositiveOrZero(message = "O carboidrato não pode ser negativo.") Double carboidrato,
        @NotNull(message = "A proteína é obrigatória.") @PositiveOrZero(message = "A proteína não pode ser negativa.") Double proteina,
        @NotNull(message = "A gordura é obrigatória.") @PositiveOrZero(message = "A gordura não pode ser negativa.") Double gordura,
        @NotNull(message = "A origem é obrigatória.") OrigemAlimento origem) {
}
