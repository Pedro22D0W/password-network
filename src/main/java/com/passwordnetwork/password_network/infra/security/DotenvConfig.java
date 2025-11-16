package com.passwordnetwork.password_network.infra.security;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        // Pular .env no GitHub Actions
        String ci = System.getenv("CI");
        if ("true".equalsIgnoreCase(ci)) {
            System.out.println("🔵 CI detectado — ignorando .env");
            return;
        }

        // Carregar .env (obrigatório no dev)
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

        System.out.println("🟢 .env carregado com sucesso");
    }
}

