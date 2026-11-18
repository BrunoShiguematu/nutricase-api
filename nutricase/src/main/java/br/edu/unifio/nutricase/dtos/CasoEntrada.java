package br.edu.unifio.nutricase.dtos;

import br.edu.unifio.nutricase.entidades.Nivel;
import jakarta.validation.constraints.*;

public record CasoEntrada(
        @NotBlank(message = "O título é obrigatório.") @Size(max = 150, message = "O título pode ter no máximo 150 caracteres.") String titulo,
        @NotBlank(message = "A modalidade é obrigatória.") @Size(max = 80, message = "A modalidade pode ter no máximo 80 caracteres.") String modalidade,
        @NotNull(message = "O nível é obrigatório.") Nivel nivel,
        @Size(max = 2000, message = "A descrição pode ter no máximo 2000 caracteres.") String descricao,
        @NotNull(message = "A tolerância da dieta é obrigatória.") @Min(value = 1, message = "A tolerância mínima é 1%.") @Max(value = 50, message = "A tolerância máxima é 50%.") Integer toleranciaDieta,
        @NotNull(message = "O atleta é obrigatório.") Long atletaId) {
}
