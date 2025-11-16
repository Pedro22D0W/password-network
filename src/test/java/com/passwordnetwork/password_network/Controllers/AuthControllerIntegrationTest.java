package com.passwordnetwork.password_network.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.passwordnetwork.password_network.Repository.PasswordRepository;
import com.passwordnetwork.password_network.Repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE) // usa o MySQL configurado via env

public class AuthControllerIntegrationTest {

@Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordRepository passwordRepository;

    @BeforeEach
    void setup() {
        passwordRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerDeveCriarUsuarioERetornarToken() throws Exception {
        String body = """
            {
              "email": "user@test.com",
              "password": "123456"
            }
            """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("user@test.com"))
            .andExpect(jsonPath("$.token", not(emptyOrNullString())));
    }

    @Test
    void loginDeveRetornarTokenQuandoCredenciaisCorretas() throws Exception {
        // 1) registra o usuário
        String registerBody = """
            {
              "email": "login@test.com",
              "password": "123456"
            }
            """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerBody))
            .andExpect(status().isOk());

        // 2) faz login com a mesma senha
        String loginBody = """
            {
              "email": "login@test.com",
              "password": "123456"
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("login@test.com"))
            .andExpect(jsonPath("$.token", not(emptyOrNullString())));
    }

    @Test
    void loginDeveRetornarBadRequestQuandoSenhaIncorreta() throws Exception {
        // registra o usuário
        String registerBody = """
            {
              "email": "wrong@test.com",
              "password": "123456"
            }
            """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerBody))
            .andExpect(status().isOk());

        // tenta logar com senha errada
        String loginBody = """
            {
              "email": "wrong@test.com",
              "password": "senha_errada"
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
            .andExpect(status().isBadRequest());
    }
}
