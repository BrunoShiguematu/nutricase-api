package br.edu.unifio.nutricase.recursos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.unifio.nutricase.dtos.CasoEntrada;
import br.edu.unifio.nutricase.entidades.Caso;
import br.edu.unifio.nutricase.servicos.CasoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import br.edu.unifio.nutricase.entidades.Nivel;
import br.edu.unifio.nutricase.entidades.Resolucao;
import br.edu.unifio.nutricase.entidades.StatusResolucao;

@RestController
@RequestMapping("casos")
@RequiredArgsConstructor
public class CasoRecurso {
    private final CasoServico servico;

    /** Consulta personalizada: GET /casos/busca?nivel=INTERMEDIARIO&modalidade=fut */
    @GetMapping("busca")
    public List<Caso> buscar(@RequestParam Nivel nivel, @RequestParam(defaultValue = "") String modalidade) {
        return servico.buscar(nivel, modalidade);
    }

    /** Consulta personalizada (JPQL): GET /casos/{id}/resolucoes?status=SUBMETIDA */
    @GetMapping("{id}/resolucoes")
    public List<Resolucao> resolucoes(@PathVariable Long id, @RequestParam StatusResolucao status) {
        return servico.resolucoesPorStatus(id, status);
    }

    @GetMapping
    public List<Caso> listar() {
        return servico.listar();
    }

    @GetMapping("{id}")
    public Caso buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Caso criar(@Valid @RequestBody CasoEntrada entrada) {
        return servico.criar(entrada);
    }

    @PutMapping("{id}")
    public Caso atualizar(@PathVariable Long id, @Valid @RequestBody CasoEntrada entrada) {
        return servico.atualizar(id, entrada);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        servico.excluir(id);
    }
}
