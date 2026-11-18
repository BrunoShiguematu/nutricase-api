# NutriCase API

API REST do **NutriCase**, desenvolvida para o Projeto Integrador do 4º termo de Engenharia de Software (UNIFIO, 2026/2), na disciplina de Padrões de Desenvolvimento Web. Este repositório é um **fork** do repositório-base de e-commerce indicado pelo professor ([sergiodelfino/ecommerce-backend-4a](https://github.com/sergiodelfino/ecommerce-backend-4a)), adaptado para o domínio do NutriCase: o projeto `ecommerce/` virou `nutricase/`, e produtos e categorias foram substituídos pelas entidades do NutriCase.

## Descrição
O NutriCase é uma plataforma educativa de **casos fictícios de nutrição esportiva**. O professor cadastra atletas e casos. O aluno resolve cada caso em uma **resolução**: calcula as próprias metas (kcal e macronutrientes) e monta a dieta em refeições com alimentos da tabela TACO. Ao **submeter**, a API corrige a dieta comparando-a com as metas que o próprio aluno calculou, dentro da tolerância definida no caso.

## Objetivo
Oferecer o back-end que guarda os casos, as resoluções e as dietas dos alunos, e que faz a correção da dieta de forma determinística, com regras de negócio, validação, tratamento de erros e controle de acesso por perfil.

## Tecnologias
- Java 21, Spring Boot 4.1.1 (Web MVC, Data JPA, Validation, Security)
- MySQL 9 e Hibernate
- Lombok
- JUnit 5, MockMvc e Spring Security Test
- Postman (coleção em `postman/`)

## Como executar
Pré-requisitos: Java 21, Maven e MySQL rodando em `localhost:3306`.

1. Crie o usuário de teste no MySQL (o banco `nutricase` é criado automaticamente):
   ```sql
   CREATE USER 'nutricase'@'localhost' IDENTIFIED BY 'nutricase123';
   GRANT ALL PRIVILEGES ON nutricase.* TO 'nutricase'@'localhost';
   ```
2. Na pasta `nutricase/`, rode a API (o Maven Wrapper dispensa ter o Maven instalado):
   ```bash
   ./mvnw spring-boot:run
   ```
   A API sobe em `http://localhost:8080`. A cada inicialização as tabelas são recriadas e preenchidas com os dados de `nutricase/src/main/resources/import.sql`.
3. Para rodar os testes automatizados (também na pasta `nutricase/`):
   ```bash
   ./mvnw test
   ```

   Outra opção é abrir o repositório no VS Code com **Dev Containers**: o container já vem com Java 21, Maven e MySQL, e cria o banco e o usuário sozinho.
4. No Postman, importe `postman/projeto-integrador.postman_collection.json` e rode as pastas em ordem, logo depois de iniciar a API.

## Usuários de teste (HTTP Basic)
| Usuário | Senha | Perfil | Pode |
|---|---|---|---|
| `professor` | `professor123` | PROFESSOR | Tudo, inclusive criar, editar e excluir atletas, casos e alimentos |
| `aluno` | `aluno123` | ALUNO | Consultar tudo e criar, editar e excluir resoluções, refeições e itens, além de submeter |

Sem credenciais, ou com senha errada, a API responde **401**. Quando o aluno tenta mexer nos cadastros do professor, responde **403**.

## Entidades
`Atleta`, `Caso`, `Alimento`, `Resolucao`, `Refeicao` e `ItemRefeicao`. O diagrama está em [`docs/diagrama-classes.md`](docs/diagrama-classes.md).

## Regras de negócio (camada service)
1. Uma **resolução submetida fica congelada**: não pode ser editada nem excluída, e não recebe novas refeições ou itens (**409**).
2. **Não se exclui o que tem histórico**: caso com resoluções, atleta com casos e alimento usado em refeições (**409**).
3. **Para submeter**, a resolução precisa ter as quatro metas preenchidas e pelo menos um alimento na dieta (**409**).
4. A **correção da dieta** compara o total da dieta com as **metas do próprio aluno**, e não com valores de referência, dentro da tolerância (%) do caso.

## Endpoints principais
Todos os recursos têm CRUD completo:

| Método | Rota | Resposta |
|---|---|---|
| GET | `/{recurso}` | 200 (lista) |
| GET | `/{recurso}/{id}` | 200 ou 404 |
| POST | `/{recurso}` | 201, 400, 404 (referência inexistente) ou 409 |
| PUT | `/{recurso}/{id}` | 200, 400, 404 ou 409 |
| DELETE | `/{recurso}/{id}` | 204, 404 ou 409 |

Os recursos são `atletas`, `casos`, `alimentos`, `resolucoes`, `refeicoes` (aceita `?resolucaoId=`) e `itens-refeicao` (aceita `?refeicaoId=`).

**Consultas personalizadas (Spring Data JPA)**
| Rota | Tipo |
|---|---|
| `GET /casos/busca?nivel=INTERMEDIARIO&modalidade=fut` | Consulta derivada do nome do método |
| `GET /alimentos/busca?nome=arroz` | Consulta derivada do nome do método |
| `GET /casos/{id}/resolucoes?status=SUBMETIDA` | `@Query` em JPQL |

**Funcionalidade específica do PI: correção da dieta**
| Rota | O que faz |
|---|---|
| `GET /resolucoes/{id}/avaliacao` | Mostra a avaliação atual sem submeter |
| `POST /resolucoes/{id}/submeter` | Valida, congela a resolução e devolve a avaliação |

Exemplo de resposta da submissão:
```json
{
  "resolucaoId": 1, "status": "SUBMETIDA", "toleranciaPercentual": 10,
  "itens": [
    { "nutriente": "kcal", "meta": 1400.0, "obtido": 1247.0, "desvioPercentual": -10.9, "dentroDaTolerancia": false },
    { "nutriente": "carboidrato", "meta": 200.0, "obtido": 196.8, "desvioPercentual": -1.6, "dentroDaTolerancia": true },
    { "nutriente": "proteina", "meta": 90.0, "obtido": 90.6, "desvioPercentual": 0.7, "dentroDaTolerancia": true },
    { "nutriente": "gordura", "meta": 25.0, "obtido": 11.1, "desvioPercentual": -55.8, "dentroDaTolerancia": false }
  ],
  "dentroDaTolerancia": 2, "total": 4, "dietaAdequada": false
}
```

## Erros
Todos os erros seguem o mesmo formato, gerado pelo `@RestControllerAdvice` em `TratadorGlobalExcecoes`:
```json
{ "momento": "...", "status": 404, "erro": "Not Found", "mensagem": "Caso com id 9999 não encontrado(a).", "campos": null }
```
No erro **400**, o campo `campos` traz a mensagem de cada campo inválido.

## Estrutura
```
postman/           coleção Postman exportada (com variáveis e respostas)
docs/              diagrama de classes
nutricase/         projeto Spring Boot
nutricase/src/main/java/br/edu/unifio/nutricase
├── entidades/     entidades JPA e enums
├── repositorios/  interfaces Spring Data JPA (com as consultas personalizadas)
├── servicos/      regras de negócio e cálculo da avaliação
├── recursos/      controllers REST (usam apenas os services)
├── dtos/          dados de entrada (com Bean Validation) e de saída
├── excecoes/      exceções personalizadas e tratador global
└── seguranca/     Spring Security (HTTP Basic, usuários em memória)
```

## Integrantes
| Nome completo | ID acadêmico |
|---|---|
| Bruno Shiguematu | 273611 |
| Guilherme Coco da Costa | 272449 |
| Matheus da Silva Gomes | 273137 |
