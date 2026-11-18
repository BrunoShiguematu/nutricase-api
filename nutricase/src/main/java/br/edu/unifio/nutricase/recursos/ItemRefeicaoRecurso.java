package br.edu.unifio.nutricase.recursos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.unifio.nutricase.dtos.ItemRefeicaoEntrada;
import br.edu.unifio.nutricase.entidades.ItemRefeicao;
import br.edu.unifio.nutricase.servicos.ItemRefeicaoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("itens-refeicao")
@RequiredArgsConstructor
public class ItemRefeicaoRecurso {
    private final ItemRefeicaoServico servico;

    @GetMapping
    public List<ItemRefeicao> listar(@RequestParam(required = false) Long refeicaoId) {
        return servico.listar(refeicaoId);
    }

    @GetMapping("{id}")
    public ItemRefeicao buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRefeicao criar(@Valid @RequestBody ItemRefeicaoEntrada entrada) {
        return servico.criar(entrada);
    }

    @PutMapping("{id}")
    public ItemRefeicao atualizar(@PathVariable Long id, @Valid @RequestBody ItemRefeicaoEntrada entrada) {
        return servico.atualizar(id, entrada);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        servico.excluir(id);
    }
}
