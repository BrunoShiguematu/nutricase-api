package br.edu.unifio.nutricase.dtos;

import br.edu.unifio.nutricase.entidades.Sexo;
import jakarta.validation.constraints.*;

public record AtletaEntrada(
        @NotBlank(message = "O nome é obrigatório.") @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres.") String nome,
        @NotNull(message = "A idade é obrigatória.") @Min(value = 10, message = "A idade mínima é 10 anos.") @Max(value = 80, message = "A idade máxima é 80 anos.") Integer idade,
        @NotNull(message = "O sexo é obrigatório.") Sexo sexo,
        @NotNull(message = "O peso é obrigatório.") @Positive(message = "O peso deve ser maior que zero.") Double peso,
        @NotNull(message = "A altura é obrigatória.") @Positive(message = "A altura deve ser maior que zero.") Double altura,
        @DecimalMin(value = "0", message = "O percentual de gordura não pode ser negativo.") @DecimalMax(value = "60", message = "O percentual de gordura máximo é 60.") Double percentualGordura,
        @Size(max = 200, message = "O objetivo pode ter no máximo 200 caracteres.") String objetivo,
        @Size(max = 100, message = "A fase pode ter no máximo 100 caracteres.") String fasePeriodizacao) {
}
