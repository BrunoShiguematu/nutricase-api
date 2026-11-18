package br.edu.unifio.nutricase.servicos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.nutricase.dtos.AvaliacaoDieta;
import br.edu.unifio.nutricase.dtos.ResolucaoEntrada;
import br.edu.unifio.nutricase.entidades.ItemRefeicao;
import br.edu.unifio.nutricase.entidades.Refeicao;
import br.edu.unifio.nutricase.entidades.Resolucao;
import br.edu.unifio.nutricase.entidades.StatusResolucao;
import br.edu.unifio.nutricase.excecoes.RecursoNaoEncontradoException;
import br.edu.unifio.nutricase.excecoes.RegraNegocioException;
import br.edu.unifio.nutricase.repositorios.ItemRefeicaoRepositorio;
import br.edu.unifio.nutricase.repositorios.RefeicaoRepositorio;
import br.edu.unifio.nutricase.repositorios.ResolucaoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResolucaoServico {
    private final ResolucaoRepositorio resolucaoRepositorio;
    private final RefeicaoRepositorio refeicaoRepositorio;
    private final ItemRefeicaoRepositorio itemRefeicaoRepositorio;
    private final CasoServico casoServico;
    private final AvaliacaoServico avaliacaoServico;

    public List<Resolucao> listar() {
        return resolucaoRepositorio.findAll(Sort.by(Sort.Direction.DESC, "data", "id"));
    }

    public Resolucao buscarPorId(Long id) {
        return resolucaoRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Resolução", id));
    }

    public Resolucao buscarEditavel(Long id) {
        Resolucao resolucao = buscarPorId(id);
        if (resolucao.getStatus() == StatusResolucao.SUBMETIDA) {
            throw new RegraNegocioException("A resolução " + id + " já foi submetida e não pode mais ser alterada.");
        }
        return resolucao;
    }

    @Transactional
    public Resolucao criar(ResolucaoEntrada entrada) {
        Resolucao r = new Resolucao();
        r.setStatus(StatusResolucao.EM_ANDAMENTO);
        return resolucaoRepositorio.save(preencher(r, entrada));
    }

    @Transactional
    public Resolucao atualizar(Long id, ResolucaoEntrada entrada) {
        return resolucaoRepositorio.save(preencher(buscarEditavel(id), entrada));
    }

    @Transactional
    public void excluir(Long id) {
        Resolucao resolucao = buscarEditavel(id);
        List<Refeicao> refeicoes = refeicaoRepositorio.findByResolucaoIdOrderByHorario(id);
        itemRefeicaoRepositorio.deleteAll(itemRefeicaoRepositorio.findByRefeicaoResolucaoId(id));
        refeicaoRepositorio.deleteAll(refeicoes);
        resolucaoRepositorio.delete(resolucao);
    }

    public AvaliacaoDieta avaliar(Long id) {
        Resolucao resolucao = buscarPorId(id);
        return avaliacaoServico.avaliar(resolucao, itemRefeicaoRepositorio.findByRefeicaoResolucaoId(id));
    }

    @Transactional
    public AvaliacaoDieta submeter(Long id) {
        Resolucao resolucao = buscarEditavel(id);
        if (resolucao.getMetaKcal() == null || resolucao.getMetaCarboidrato() == null
                || resolucao.getMetaProteina() == null || resolucao.getMetaGordura() == null) {
            throw new RegraNegocioException("Preencha as quatro metas (kcal, carboidrato, proteína e gordura) antes de submeter.");
        }
        List<ItemRefeicao> itens = itemRefeicaoRepositorio.findByRefeicaoResolucaoId(id);
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Monte a dieta com pelo menos um alimento antes de submeter.");
        }
        resolucao.setStatus(StatusResolucao.SUBMETIDA);
        resolucao.setSubmetidaEm(LocalDateTime.now());
        resolucaoRepositorio.save(resolucao);
        return avaliacaoServico.avaliar(resolucao, itens);
    }

    private Resolucao preencher(Resolucao r, ResolucaoEntrada e) {
        r.setCaso(casoServico.buscarPorId(e.casoId()));
        r.setData(e.data() == null ? LocalDate.now() : e.data());
        r.setJustificativa(e.justificativa());
        r.setMetaKcal(e.metaKcal());
        r.setMetaCarboidrato(e.metaCarboidrato());
        r.setMetaProteina(e.metaProteina());
        r.setMetaGordura(e.metaGordura());
        return r;
    }
}
