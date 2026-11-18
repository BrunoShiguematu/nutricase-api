# Diagrama de classes — NutriCase API

```mermaid
classDiagram
    direction LR
    class Atleta {
        Long id
        String nome
        Integer idade
        Sexo sexo
        Double peso
        Double altura
        Double percentualGordura
        String objetivo
        String fasePeriodizacao
    }
    class Caso {
        Long id
        String titulo
        String modalidade
        Nivel nivel
        String descricao
        Integer toleranciaDieta
    }
    class Alimento {
        Long id
        String nome
        String grupo
        Double kcal
        Double carboidrato
        Double proteina
        Double gordura
        OrigemAlimento origem
    }
    class Resolucao {
        Long id
        LocalDate data
        StatusResolucao status
        String justificativa
        Double metaKcal
        Double metaCarboidrato
        Double metaProteina
        Double metaGordura
        LocalDateTime submetidaEm
    }
    class Refeicao {
        Long id
        String nome
        LocalTime horario
        MarcadorRefeicao marcador
    }
    class ItemRefeicao {
        Long id
        Double quantidadeGramas
    }
    class Sexo { <<enumeration>> FEMININO MASCULINO }
    class Nivel { <<enumeration>> INICIANTE INTERMEDIARIO AVANCADO }
    class OrigemAlimento { <<enumeration>> TACO ALUNO }
    class StatusResolucao { <<enumeration>> EM_ANDAMENTO SUBMETIDA }
    class MarcadorRefeicao { <<enumeration>> PRE_TREINO POS_TREINO NENHUM }

    Atleta "1" <-- "0..*" Caso : atleta
    Caso "1" <-- "0..*" Resolucao : caso
    Resolucao "1" <-- "0..*" Refeicao : resolucao
    Refeicao "1" <-- "0..*" ItemRefeicao : refeicao
    Alimento "1" <-- "0..*" ItemRefeicao : alimento
```

No banco (MySQL), cada seta vira uma chave estrangeira: `caso.atleta_id`, `resolucao.caso_id`, `refeicao.resolucao_id`, `item_refeicao.refeicao_id` e `item_refeicao.alimento_id`.
