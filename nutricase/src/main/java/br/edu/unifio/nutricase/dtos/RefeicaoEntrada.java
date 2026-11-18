package br.edu.unifio.nutricase.dtos;

import java.time.LocalTime;

import br.edu.unifio.nutricase.entidades.MarcadorRefeicao;
import jakarta.validation.constraints.*;

public record RefeicaoEntrada(
        @NotNull(message = "A resolução é obrigatória.") Long resolucaoId,
        @NotBlank(message = "O nome é obrigatório.") @Size(max = 60, message = "O nome pode ter no máximo 60 caracteres.") String nome,
        @NotNull(message = "O horário é obrigatório.") LocalTime horario,
        MarcadorRefeicao marcador) {
}
