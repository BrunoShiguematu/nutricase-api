package br.edu.unifio.nutricase.recursos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.edu.unifio.nutricase.dtos.AtletaEntrada;
import br.edu.unifio.nutricase.entidades.Atleta;
import br.edu.unifio.nutricase.servicos.AtletaServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("atletas")
@RequiredArgsConstructor
public class AtletaRecurso {
    private final AtletaServico servico;

    @GetMapping
    public List<Atleta> listar() {
        return servico.listar();
    }

    @GetMapping("{id}")
    public Atleta buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Atleta criar(@Valid @RequestBody AtletaEntrada entrada) {
        return servico.criar(entrada);
    }

    @PutMapping("{id}")
    public Atleta atualizar(@PathVariable Long id, @Valid @RequestBody AtletaEntrada entrada) {
        return servico.atualizar(id, entrada);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        servico.excluir(id);
    }
}
