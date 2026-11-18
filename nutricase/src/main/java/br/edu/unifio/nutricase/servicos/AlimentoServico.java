package br.edu.unifio.nutricase.servicos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.nutricase.dtos.AlimentoEntrada;
import br.edu.unifio.nutricase.entidades.Alimento;
import br.edu.unifio.nutricase.excecoes.RecursoNaoEncontradoException;
import br.edu.unifio.nutricase.excecoes.RegraNegocioException;
import br.edu.unifio.nutricase.repositorios.AlimentoRepositorio;
import br.edu.unifio.nutricase.repositorios.ItemRefeicaoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlimentoServico {
    private final AlimentoRepositorio alimentoRepositorio;
    private final ItemRefeicaoRepositorio itemRefeicaoRepositorio;

    public List<Alimento> listar() {
        return alimentoRepositorio.findAll(Sort.by("nome"));
    }

    public Alimento buscarPorId(Long id) {
        return alimentoRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Alimento", id));
    }

    public List<Alimento> buscarPorNome(String nome) {
        return alimentoRepositorio.findByNomeContainingIgnoreCaseOrderByNome(nome == null ? "" : nome.trim());
    }

    @Transactional
    public Alimento criar(AlimentoEntrada entrada) {
        return alimentoRepositorio.save(preencher(new Alimento(), entrada));
    }

    @Transactional
    public Alimento atualizar(Long id, AlimentoEntrada entrada) {
        return alimentoRepositorio.save(preencher(buscarPorId(id), entrada));
    }

    @Transactional
    public void excluir(Long id) {
        Alimento alimento = buscarPorId(id);
        if (itemRefeicaoRepositorio.existsByAlimentoId(id)) {
            throw new RegraNegocioException("O alimento não pode ser excluído porque está sendo usado em refeições.");
        }
        alimentoRepositorio.delete(alimento);
    }

    private Alimento preencher(Alimento a, AlimentoEntrada e) {
        a.setNome(e.nome().trim());
        a.setGrupo(e.grupo());
        a.setKcal(e.kcal());
        a.setCarboidrato(e.carboidrato());
        a.setProteina(e.proteina());
        a.setGordura(e.gordura());
        a.setOrigem(e.origem());
        return a;
    }
}
