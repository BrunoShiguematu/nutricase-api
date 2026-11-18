package br.edu.unifio.nutricase.servicos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.nutricase.dtos.RefeicaoEntrada;
import br.edu.unifio.nutricase.entidades.Refeicao;
import br.edu.unifio.nutricase.entidades.MarcadorRefeicao;
import br.edu.unifio.nutricase.excecoes.RecursoNaoEncontradoException;
import br.edu.unifio.nutricase.repositorios.ItemRefeicaoRepositorio;
import br.edu.unifio.nutricase.repositorios.RefeicaoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefeicaoServico {
    private final RefeicaoRepositorio refeicaoRepositorio;
    private final ItemRefeicaoRepositorio itemRefeicaoRepositorio;
    private final ResolucaoServico resolucaoServico;

    public List<Refeicao> listar(Long resolucaoId) {
        if (resolucaoId != null) {
            resolucaoServico.buscarPorId(resolucaoId);
            return refeicaoRepositorio.findByResolucaoIdOrderByHorario(resolucaoId);
        }
        return refeicaoRepositorio.findAll(Sort.by("id"));
    }

    public Refeicao buscarPorId(Long id) {
        return refeicaoRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Refeição", id));
    }

    @Transactional
    public Refeicao criar(RefeicaoEntrada entrada) {
        return refeicaoRepositorio.save(preencher(new Refeicao(), entrada));
    }

    @Transactional
    public Refeicao atualizar(Long id, RefeicaoEntrada entrada) {
        Refeicao refeicao = buscarPorId(id);
        resolucaoServico.buscarEditavel(refeicao.getResolucao().getId());
        return refeicaoRepositorio.save(preencher(refeicao, entrada));
    }

    @Transactional
    public void excluir(Long id) {
        Refeicao refeicao = buscarPorId(id);
        resolucaoServico.buscarEditavel(refeicao.getResolucao().getId());
        itemRefeicaoRepositorio.deleteAll(itemRefeicaoRepositorio.findByRefeicaoId(id));
        refeicaoRepositorio.delete(refeicao);
    }

    private Refeicao preencher(Refeicao r, RefeicaoEntrada e) {
        r.setResolucao(resolucaoServico.buscarEditavel(e.resolucaoId()));
        r.setNome(e.nome().trim());
        r.setHorario(e.horario());
        r.setMarcador(e.marcador() == null ? MarcadorRefeicao.NENHUM : e.marcador());
        return r;
    }
}
