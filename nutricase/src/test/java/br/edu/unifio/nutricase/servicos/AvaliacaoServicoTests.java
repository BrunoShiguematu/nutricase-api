package br.edu.unifio.nutricase.servicos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.edu.unifio.nutricase.dtos.AvaliacaoDieta;
import br.edu.unifio.nutricase.dtos.AvaliacaoMacro;
import br.edu.unifio.nutricase.entidades.Alimento;
import br.edu.unifio.nutricase.entidades.Caso;
import br.edu.unifio.nutricase.entidades.ItemRefeicao;
import br.edu.unifio.nutricase.entidades.Resolucao;

/** Teste unitário do cálculo (sem banco): valores por 100 g x quantidade. */
class AvaliacaoServicoTests {
    private final AvaliacaoServico servico = new AvaliacaoServico();

    private static ItemRefeicao item(double kcal, double carb, double prot, double gord, double gramas) {
        Alimento a = new Alimento();
        a.setKcal(kcal); a.setCarboidrato(carb); a.setProteina(prot); a.setGordura(gord);
        ItemRefeicao i = new ItemRefeicao();
        i.setAlimento(a); i.setQuantidadeGramas(gramas);
        return i;
    }

    private static Resolucao resolucao(int tolerancia, double kcal, double carb, double prot, double gord) {
        Caso c = new Caso(); c.setToleranciaDieta(tolerancia);
        Resolucao r = new Resolucao(); r.setCaso(c);
        r.setMetaKcal(kcal); r.setMetaCarboidrato(carb); r.setMetaProteina(prot); r.setMetaGordura(gord);
        return r;
    }

    @Test
    void dietaExatamenteNaMetaFicaDentroEmTudo() {
        // 200 g de um alimento com 100 kcal, 10 C, 5 P, 2 G por 100 g -> 200 kcal, 20 C, 10 P, 4 G
        AvaliacaoDieta a = servico.avaliar(resolucao(10, 200, 20, 10, 4), List.of(item(100, 10, 5, 2, 200)));
        assertTrue(a.dietaAdequada());
        assertEquals(4, a.dentroDaTolerancia());
        a.itens().forEach(m -> assertEquals(0.0, m.desvioPercentual()));
    }

    @Test
    void desvioAcimaDaToleranciaFicaFora() {
        // obtido 120 kcal x meta 100 -> +20% (tolerância 10%)
        AvaliacaoDieta a = servico.avaliar(resolucao(10, 100, 10, 5, 2), List.of(item(120, 10, 5, 2, 100)));
        AvaliacaoMacro kcal = a.itens().get(0);
        assertEquals(120.0, kcal.obtido());
        assertEquals(20.0, kcal.desvioPercentual());
        assertFalse(kcal.dentroDaTolerancia());
        assertFalse(a.dietaAdequada());
        assertEquals(3, a.dentroDaTolerancia());
    }

    @Test
    void desvioNoLimiteDaToleranciaContaComoDentro() {
        AvaliacaoDieta a = servico.avaliar(resolucao(10, 100, 10, 5, 2), List.of(item(90, 10, 5, 2, 100)));
        assertEquals(-10.0, a.itens().get(0).desvioPercentual());
        assertTrue(a.itens().get(0).dentroDaTolerancia());
    }

    @Test
    void somaVariosItens() {
        AvaliacaoDieta a = servico.avaliar(resolucao(10, 300, 30, 15, 6),
                List.of(item(100, 10, 5, 2, 100), item(100, 10, 5, 2, 200)));
        assertEquals(300.0, a.itens().get(0).obtido());
        assertTrue(a.dietaAdequada());
    }
}
