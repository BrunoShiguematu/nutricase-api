package br.edu.unifio.nutricase.servicos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.nutricase.dtos.CasoEntrada;
import br.edu.unifio.nutricase.entidades.Caso;
import br.edu.unifio.nutricase.entidades.Nivel;
import br.edu.unifio.nutricase.entidades.Resolucao;
import br.edu.unifio.nutricase.entidades.StatusResolucao;
import br.edu.unifio.nutricase.excecoes.RecursoNaoEncontradoException;
import br.edu.unifio.nutricase.excecoes.RegraNegocioException;
import br.edu.unifio.nutricase.repositorios.CasoRepositorio;
import br.edu.unifio.nutricase.repositorios.ResolucaoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CasoServico {
    private final CasoRepositorio casoRepositorio;
    private final ResolucaoRepositorio resolucaoRepositorio;
    private final AtletaServico atletaServico;

    public List<Caso> listar() {
        return casoRepositorio.findAll(Sort.by("titulo"));
    }

    public Caso buscarPorId(Long id) {
        return casoRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Caso", id));
    }

    public List<Caso> buscar(Nivel nivel, String modalidade) {
        return casoRepositorio.findByNivelAndModalidadeContainingIgnoreCaseOrderByTitulo(nivel, modalidade == null ? "" : modalidade.trim());
    }

    public List<Resolucao> resolucoesPorStatus(Long casoId, StatusResolucao status) {
        buscarPorId(casoId);
        return resolucaoRepositorio.buscarPorCasoEStatus(casoId, status);
    }

    @Transactional
    public Caso criar(CasoEntrada entrada) {
        return casoRepositorio.save(preencher(new Caso(), entrada));
    }

    @Transactional
    public Caso atualizar(Long id, CasoEntrada entrada) {
        return casoRepositorio.save(preencher(buscarPorId(id), entrada));
    }

    /** Regra: caso que já tem resoluções não pode ser excluído (preserva o histórico dos alunos). */
    @Transactional
    public void excluir(Long id) {
        Caso caso = buscarPorId(id);
        if (resolucaoRepositorio.existsByCasoId(id)) {
            throw new RegraNegocioException("O caso não pode ser excluído porque já possui resoluções de alunos.");
        }
        casoRepositorio.delete(caso);
    }

    private Caso preencher(Caso c, CasoEntrada e) {
        c.setTitulo(e.titulo().trim());
        c.setModalidade(e.modalidade().trim());
        c.setNivel(e.nivel());
        c.setDescricao(e.descricao());
        c.setToleranciaDieta(e.toleranciaDieta());
        c.setAtleta(atletaServico.buscarPorId(e.atletaId()));
        return c;
    }
}
