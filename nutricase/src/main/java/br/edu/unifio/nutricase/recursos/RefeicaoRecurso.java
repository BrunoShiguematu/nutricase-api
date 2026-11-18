package br.edu.unifio.nutricase.recursos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.unifio.nutricase.dtos.RefeicaoEntrada;
import br.edu.unifio.nutricase.entidades.Refeicao;
import br.edu.unifio.nutricase.servicos.RefeicaoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("refeicoes")
@RequiredArgsConstructor
public class RefeicaoRecurso {
    private final RefeicaoServico servico;

    @GetMapping
    public List<Refeicao> listar(@RequestParam(required = false) Long resolucaoId) {
        return servico.listar(resolucaoId);
    }

    @GetMapping("{id}")
    public Refeicao buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Refeicao criar(@Valid @RequestBody RefeicaoEntrada entrada) {
        return servico.criar(entrada);
    }

    @PutMapping("{id}")
    public Refeicao atualizar(@PathVariable Long id, @Valid @RequestBody RefeicaoEntrada entrada) {
        return servico.atualizar(id, entrada);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        servico.excluir(id);
    }
}
