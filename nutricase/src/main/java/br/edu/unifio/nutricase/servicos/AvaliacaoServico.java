package br.edu.unifio.nutricase.servicos;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.unifio.nutricase.dtos.AvaliacaoDieta;
import br.edu.unifio.nutricase.dtos.AvaliacaoMacro;
import br.edu.unifio.nutricase.entidades.Alimento;
import br.edu.unifio.nutricase.entidades.ItemRefeicao;
import br.edu.unifio.nutricase.entidades.Resolucao;

/**
 * Cálculo determinístico da avaliação da dieta.
 * A dieta é comparada com as metas que o PRÓPRIO aluno calculou, não com valores de referência.
 * Valores dos alimentos são por 100 g.
 */
@Service
public class AvaliacaoServico {

    public AvaliacaoDieta avaliar(Resolucao resolucao, List<ItemRefeicao> itens) {
        double kcal = 0, carb = 0, prot = 0, gord = 0;
        for (ItemRefeicao item : itens) {
            Alimento a = item.getAlimento();
            double fator = item.getQuantidadeGramas() / 100.0;
            kcal += a.getKcal() * fator;
            carb += a.getCarboidrato() * fator;
            prot += a.getProteina() * fator;
            gord += a.getGordura() * fator;
        }
        Integer tol = resolucao.getCaso().getToleranciaDieta();
        int tolerancia = tol == null ? 10 : tol;
        List<AvaliacaoMacro> macros = List.of(
                comparar("kcal", resolucao.getMetaKcal(), kcal, tolerancia),
                comparar("carboidrato", resolucao.getMetaCarboidrato(), carb, tolerancia),
                comparar("proteina", resolucao.getMetaProteina(), prot, tolerancia),
                comparar("gordura", resolucao.getMetaGordura(), gord, tolerancia));
        int dentro = (int) macros.stream().filter(AvaliacaoMacro::dentroDaTolerancia).count();
        return new AvaliacaoDieta(resolucao.getId(), resolucao.getStatus(), tolerancia, macros, dentro, macros.size(),
                dentro == macros.size());
    }

    private AvaliacaoMacro comparar(String nome, Double meta, double obtido, int tolerancia) {
        double alvo = meta == null ? 0 : meta;
        double desvio = alvo == 0 ? 0 : (obtido - alvo) / alvo * 100.0;
        boolean dentro = alvo > 0 && Math.abs(desvio) <= tolerancia;
        return new AvaliacaoMacro(nome, alvo, arredondar(obtido), arredondar(desvio), dentro);
    }

    private static double arredondar(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
