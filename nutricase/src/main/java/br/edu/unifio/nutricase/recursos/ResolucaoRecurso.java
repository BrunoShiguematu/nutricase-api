package br.edu.unifio.nutricase.recursos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.unifio.nutricase.dtos.ResolucaoEntrada;
import br.edu.unifio.nutricase.entidades.Resolucao;
import br.edu.unifio.nutricase.servicos.ResolucaoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import br.edu.unifio.nutricase.dtos.AvaliacaoDieta;

@RestController
@RequestMapping("resolucoes")
@RequiredArgsConstructor
public class ResolucaoRecurso {
    private final ResolucaoServico servico;

    /** Relatório da dieta contra as metas do aluno, sem submeter. */
    @GetMapping("{id}/avaliacao")
    public AvaliacaoDieta avaliar(@PathVariable Long id) {
        return servico.avaliar(id);
    }

    /** Funcionalidade específica do PI: submete a resolução e devolve a avaliação. */
    @PostMapping("{id}/submeter")
    public AvaliacaoDieta submeter(@PathVariable Long id) {
        return servico.submeter(id);
    }

    @GetMapping
    public List<Resolucao> listar() {
        return servico.listar();
    }

    @GetMapping("{id}")
    public Resolucao buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resolucao criar(@Valid @RequestBody ResolucaoEntrada entrada) {
        return servico.criar(entrada);
    }

    @PutMapping("{id}")
    public Resolucao atualizar(@PathVariable Long id, @Valid @RequestBody ResolucaoEntrada entrada) {
        return servico.atualizar(id, entrada);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        servico.excluir(id);
    }
}
