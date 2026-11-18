package br.edu.unifio.nutricase.excecoes;

/** Lançada quando uma operação viola uma regra de negócio (vira HTTP 409). */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
