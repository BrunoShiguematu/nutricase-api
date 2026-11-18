package br.edu.unifio.nutricase.repositorios;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.unifio.nutricase.entidades.Alimento;
import br.edu.unifio.nutricase.entidades.Caso;
import br.edu.unifio.nutricase.entidades.Nivel;
import br.edu.unifio.nutricase.entidades.Resolucao;
import br.edu.unifio.nutricase.entidades.StatusResolucao;

@SpringBootTest
class ConsultasRepositorioTests {
    @Autowired private CasoRepositorio casoRepositorio;
    @Autowired private AlimentoRepositorio alimentoRepositorio;
    @Autowired private ResolucaoRepositorio resolucaoRepositorio;

    @Test
    void deveBuscarCasosPorNivelEModalidade() {
        List<Caso> casos = casoRepositorio.findByNivelAndModalidadeContainingIgnoreCaseOrderByTitulo(Nivel.INTERMEDIARIO, "fut");
        assertEquals(1, casos.size());
        assertEquals("Meio-campista na pré-temporada", casos.get(0).getTitulo());
    }

    @Test
    void naoDeveRetornarCasoDeOutroNivel() {
        assertTrue(casoRepositorio.findByNivelAndModalidadeContainingIgnoreCaseOrderByTitulo(Nivel.AVANCADO, "futebol").isEmpty());
    }

    @Test
    void deveBuscarAlimentosPeloNomeEmOrdemAlfabetica() {
        List<Alimento> alimentos = alimentoRepositorio.findByNomeContainingIgnoreCaseOrderByNome("o");
        assertFalse(alimentos.isEmpty());
        assertEquals("Arroz branco cozido", alimentos.get(0).getNome());
        assertEquals(1, alimentoRepositorio.findByNomeContainingIgnoreCaseOrderByNome("BANANA").size());
    }

    @Test
    void deveBuscarResolucoesDoCasoPorStatusComJpql() {
        List<Resolucao> submetidas = resolucaoRepositorio.buscarPorCasoEStatus(1L, StatusResolucao.SUBMETIDA);
        assertEquals(1, submetidas.size());
        assertTrue(resolucaoRepositorio.buscarPorCasoEStatus(1L, StatusResolucao.EM_ANDAMENTO).isEmpty());
    }
}
