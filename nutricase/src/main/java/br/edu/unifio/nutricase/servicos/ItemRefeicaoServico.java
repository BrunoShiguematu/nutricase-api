package br.edu.unifio.nutricase.servicos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.nutricase.dtos.ItemRefeicaoEntrada;
import br.edu.unifio.nutricase.entidades.ItemRefeicao;
import br.edu.unifio.nutricase.entidades.Refeicao;
import br.edu.unifio.nutricase.excecoes.RecursoNaoEncontradoException;
import br.edu.unifio.nutricase.repositorios.ItemRefeicaoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemRefeicaoServico {
    private final ItemRefeicaoRepositorio itemRefeicaoRepositorio;
    private final RefeicaoServico refeicaoServico;
    private final AlimentoServico alimentoServico;
    private final ResolucaoServico resolucaoServico;

    public List<ItemRefeicao> listar(Long refeicaoId) {
        if (refeicaoId != null) {
            refeicaoServico.buscarPorId(refeicaoId);
            return itemRefeicaoRepositorio.findByRefeicaoId(refeicaoId);
        }
        return itemRefeicaoRepositorio.findAll(Sort.by("id"));
    }

    public ItemRefeicao buscarPorId(Long id) {
        return itemRefeicaoRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Item de refeição", id));
    }

    @Transactional
    public ItemRefeicao criar(ItemRefeicaoEntrada entrada) {
        return itemRefeicaoRepositorio.save(preencher(new ItemRefeicao(), entrada));
    }

    @Transactional
    public ItemRefeicao atualizar(Long id, ItemRefeicaoEntrada entrada) {
        ItemRefeicao item = buscarPorId(id);
        verificarEditavel(item.getRefeicao());
        return itemRefeicaoRepositorio.save(preencher(item, entrada));
    }

    @Transactional
    public void excluir(Long id) {
        ItemRefeicao item = buscarPorId(id);
        verificarEditavel(item.getRefeicao());
        itemRefeicaoRepositorio.delete(item);
    }

    private void verificarEditavel(Refeicao refeicao) {
        resolucaoServico.buscarEditavel(refeicao.getResolucao().getId());
    }

    private ItemRefeicao preencher(ItemRefeicao i, ItemRefeicaoEntrada e) {
        Refeicao refeicao = refeicaoServico.buscarPorId(e.refeicaoId());
        verificarEditavel(refeicao);
        i.setRefeicao(refeicao);
        i.setAlimento(alimentoServico.buscarPorId(e.alimentoId()));
        i.setQuantidadeGramas(e.quantidadeGramas());
        return i;
    }
}
