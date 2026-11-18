package br.edu.unifio.nutricase.recursos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.unifio.nutricase.dtos.AlimentoEntrada;
import br.edu.unifio.nutricase.entidades.Alimento;
import br.edu.unifio.nutricase.servicos.AlimentoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("alimentos")
@RequiredArgsConstructor
public class AlimentoRecurso {
    private final AlimentoServico servico;

    /** Consulta personalizada: GET /alimentos/busca?nome=arroz */
    @GetMapping("busca")
    public List<Alimento> buscarPorNome(@RequestParam(defaultValue = "") String nome) {
        return servico.buscarPorNome(nome);
    }

    @GetMapping
    public List<Alimento> listar() {
        return servico.listar();
    }

    @GetMapping("{id}")
    public Alimento buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Alimento criar(@Valid @RequestBody AlimentoEntrada entrada) {
        return servico.criar(entrada);
    }

    @PutMapping("{id}")
    public Alimento atualizar(@PathVariable Long id, @Valid @RequestBody AlimentoEntrada entrada) {
        return servico.atualizar(id, entrada);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        servico.excluir(id);
    }
}
