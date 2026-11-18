package br.edu.unifio.nutricase.excecoes;

import java.time.LocalDateTime;
import java.util.Map;

/** Formato padrão das respostas de erro da API. */
public record RespostaErro(LocalDateTime momento, int status, String erro, String mensagem, Map<String, String> campos) {
}
