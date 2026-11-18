package br.edu.unifio.nutricase.recursos;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/** Testes de ponta a ponta da API (cada teste é desfeito ao final). */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiIntegracaoTests {
    @Autowired private MockMvc mvc;

    /** Extrai o primeiro "id" do JSON (o do próprio registro criado). */
    private static String primeiroId(String json) {
        var m = java.util.regex.Pattern.compile("\"id\":(\\d+)").matcher(json);
        return m.find() ? m.group(1) : "";
    }

    private static final String ATLETA = """
            {"nome":"Ana Teste","idade":20,"sexo":"FEMININO","peso":60,"altura":168,"percentualGordura":20,
             "objetivo":"Teste","fasePeriodizacao":"Base"}""";

    @Test
    void semAutenticacaoRetorna401() throws Exception {
        mvc.perform(get("/casos")).andExpect(status().isUnauthorized());
    }

    @Test
    void senhaErradaRetorna401() throws Exception {
        mvc.perform(get("/casos").with(httpBasic("aluno", "errada"))).andExpect(status().isUnauthorized());
    }

    @Test
    void alunoNaoPodeCriarAtletaRetorna403() throws Exception {
        mvc.perform(post("/atletas").with(httpBasic("aluno", "aluno123"))
                .contentType(MediaType.APPLICATION_JSON).content(ATLETA))
            .andExpect(status().isForbidden());
    }

    @Test
    void professorFazCrudCompletoDeAtleta() throws Exception {
        String corpo = mvc.perform(post("/atletas").with(httpBasic("professor", "professor123"))
                .contentType(MediaType.APPLICATION_JSON).content(ATLETA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn().getResponse().getContentAsString();
        String id = primeiroId(corpo);
        mvc.perform(get("/atletas/" + id).with(httpBasic("aluno", "aluno123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Ana Teste"));
        mvc.perform(put("/atletas/" + id).with(httpBasic("professor", "professor123"))
                .contentType(MediaType.APPLICATION_JSON).content(ATLETA.replace("Ana Teste", "Ana Editada")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Ana Editada"));
        mvc.perform(delete("/atletas/" + id).with(httpBasic("professor", "professor123")))
            .andExpect(status().isNoContent());
    }

    @Test
    void dadosInvalidosRetornam400ComCampos() throws Exception {
        mvc.perform(post("/atletas").with(httpBasic("professor", "professor123"))
                .contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"\",\"idade\":5,\"peso\":-1}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.campos.nome").exists())
            .andExpect(jsonPath("$.campos.idade").exists());
    }

    @Test
    void recursoInexistenteRetorna404() throws Exception {
        mvc.perform(get("/casos/9999").with(httpBasic("aluno", "aluno123")))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.mensagem", containsString("9999")));
    }

    @Test
    void excluirCasoComResolucoesRetorna409() throws Exception {
        mvc.perform(delete("/casos/1").with(httpBasic("professor", "professor123")))
            .andExpect(status().isConflict());
    }

    @Test
    void excluirAlimentoEmUsoRetorna409() throws Exception {
        mvc.perform(delete("/alimentos/1").with(httpBasic("professor", "professor123")))
            .andExpect(status().isConflict());
    }

    @Test
    void editarResolucaoSubmetidaRetorna409() throws Exception {
        mvc.perform(put("/resolucoes/2").with(httpBasic("aluno", "aluno123"))
                .contentType(MediaType.APPLICATION_JSON).content("{\"casoId\":1,\"justificativa\":\"x\"}"))
            .andExpect(status().isConflict());
        mvc.perform(post("/refeicoes").with(httpBasic("aluno", "aluno123"))
                .contentType(MediaType.APPLICATION_JSON).content("{\"resolucaoId\":2,\"nome\":\"Ceia\",\"horario\":\"22:00\"}"))
            .andExpect(status().isConflict());
    }

    @Test
    void submeterResolucaoDevolveAvaliacaoEcongela() throws Exception {
        mvc.perform(post("/resolucoes/1/submeter").with(httpBasic("aluno", "aluno123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUBMETIDA"))
            .andExpect(jsonPath("$.itens", hasSize(4)))
            .andExpect(jsonPath("$.itens[0].obtido").value(1247.0))
            .andExpect(jsonPath("$.itens[1].dentroDaTolerancia").value(true))
            .andExpect(jsonPath("$.dietaAdequada").value(false));
        mvc.perform(post("/resolucoes/1/submeter").with(httpBasic("aluno", "aluno123")))
            .andExpect(status().isConflict());
    }

    @Test
    void submeterSemDietaRetorna409() throws Exception {
        String corpo = mvc.perform(post("/resolucoes").with(httpBasic("aluno", "aluno123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"casoId\":3,\"metaKcal\":3000,\"metaCarboidrato\":400,\"metaProteina\":120,\"metaGordura\":80}"))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = primeiroId(corpo);
        mvc.perform(post("/resolucoes/" + id + "/submeter").with(httpBasic("aluno", "aluno123")))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.mensagem", containsString("pelo menos um alimento")));
    }

    @Test
    void consultasPersonalizadasPelaApi() throws Exception {
        mvc.perform(get("/casos/busca").param("nivel", "INICIANTE").param("modalidade", "nata").with(httpBasic("aluno", "aluno123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        mvc.perform(get("/casos/1/resolucoes").param("status", "SUBMETIDA").with(httpBasic("aluno", "aluno123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        mvc.perform(get("/alimentos/busca").param("nome", "arroz").with(httpBasic("aluno", "aluno123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$[0].nome").value("Arroz branco cozido"));
    }
}
