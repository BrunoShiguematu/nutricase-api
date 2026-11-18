package br.edu.unifio.nutricase.seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class ConfiguracaoSeguranca {

    private static final String[] CADASTROS_DO_PROFESSOR = { "/atletas/**", "/casos/**", "/alimentos/**" };

    @Bean
    public SecurityFilterChain filtro(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error").permitAll()
                .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("ALUNO", "PROFESSOR")
                .requestMatchers(HttpMethod.POST, CADASTROS_DO_PROFESSOR).hasRole("PROFESSOR")
                .requestMatchers(HttpMethod.PUT, CADASTROS_DO_PROFESSOR).hasRole("PROFESSOR")
                .requestMatchers(HttpMethod.DELETE, CADASTROS_DO_PROFESSOR).hasRole("PROFESSOR")
                .anyRequest().hasAnyRole("ALUNO", "PROFESSOR"))
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.setHeader("WWW-Authenticate", "Basic realm=\"NutriCase\"");
                    erro(res, 401, "Unauthorized", "Autenticação necessária: informe usuário e senha válidos (HTTP Basic).");
                })
                .accessDeniedHandler((req, res, e) ->
                    erro(res, 403, "Forbidden", "Seu perfil não tem permissão para esta operação.")));
        return http.build();
    }

    private static void erro(HttpServletResponse res, int status, String erro, String mensagem) throws java.io.IOException {
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("{\"momento\":\"" + java.time.LocalDateTime.now() + "\",\"status\":" + status
                + ",\"erro\":\"" + erro + "\",\"mensagem\":\"" + mensagem + "\",\"campos\":null}");
    }

    @Bean
    public PasswordEncoder codificadorSenha() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService usuarios(PasswordEncoder codificador) {
        return new InMemoryUserDetailsManager(
            User.withUsername("professor").password(codificador.encode("professor123")).roles("PROFESSOR").build(),
            User.withUsername("aluno").password(codificador.encode("aluno123")).roles("ALUNO").build());
    }
}
