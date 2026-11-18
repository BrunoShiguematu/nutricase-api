package br.edu.unifio.nutricase.servicos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.nutricase.dtos.AtletaEntrada;
import br.edu.unifio.nutricase.entidades.Atleta;
import br.edu.unifio.nutricase.excecoes.RecursoNaoEncontradoException;
import br.edu.unifio.nutricase.excecoes.RegraNegocioException;
import br.edu.unifio.nutricase.repositorios.AtletaRepositorio;
import br.edu.unifio.nutricase.repositorios.CasoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtletaServico {
    private final AtletaRepositorio atletaRepositorio;
    private final CasoRepositorio casoRepositorio;

    public List<Atleta> listar() {
        return atletaRepositorio.findAll(Sort.by("nome"));
    }

    public Atleta buscarPorId(Long id) {
        return atletaRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Atleta", id));
    }

    @Transactional
    public Atleta criar(AtletaEntrada entrada) {
        return atletaRepositorio.save(preencher(new Atleta(), entrada));
    }

    @Transactional
    public Atleta atualizar(Long id, AtletaEntrada entrada) {
        return atletaRepositorio.save(preencher(buscarPorId(id), entrada));
    }

    @Transactional
    public void excluir(Long id) {
        Atleta atleta = buscarPorId(id);
        if (casoRepositorio.existsByAtletaId(id)) {
            throw new RegraNegocioException("O atleta não pode ser excluído porque está vinculado a casos.");
        }
        atletaRepositorio.delete(atleta);
    }

    private Atleta preencher(Atleta a, AtletaEntrada e) {
        a.setNome(e.nome().trim());
        a.setIdade(e.idade());
        a.setSexo(e.sexo());
        a.setPeso(e.peso());
        a.setAltura(e.altura());
        a.setPercentualGordura(e.percentualGordura());
        a.setObjetivo(e.objetivo());
        a.setFasePeriodizacao(e.fasePeriodizacao());
        return a;
    }
}
