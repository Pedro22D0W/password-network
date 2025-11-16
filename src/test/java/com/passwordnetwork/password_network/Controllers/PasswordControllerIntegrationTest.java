package com.passwordnetwork.password_network.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.passwordnetwork.password_network.Models.Password;
import com.passwordnetwork.password_network.Models.User;
import com.passwordnetwork.password_network.Repository.PasswordRepository;
import com.passwordnetwork.password_network.Repository.UserRepository;
import com.passwordnetwork.password_network.Services.PasswordStrengthService;
import com.passwordnetwork.password_network.infra.security.TokenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class PasswordControllerIntegrationTest {

@Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordRepository passwordRepository;

    @MockBean
    TokenService tokenService;

    @MockBean
    PasswordStrengthService passwordStrengthService;

    @BeforeEach
    void setup() {
        passwordRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createStrongPasswordDeveGerarESalvarSenhaParaUsuarioDoToken() throws Exception {
        String email = "user@test.com";

        // cria usuário no banco
        User user = new User();
        user.setEmail(email);
        user.setPassword("qualquer"); // não importa aqui
        user = userRepository.save(user);

        // configura mocks
        when(tokenService.ValidateToken("fake-token")).thenReturn(email);
        when(passwordStrengthService.strengthenPassword("senhaFraca"))
                .thenReturn("SenhaF0rte!");

        String body = """
            {
              "plataform": "github",
              "password": "senhaFraca"
            }
            """;

        mockMvc.perform(post("/password/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer fake-token")
                .content(body))
            .andExpect(status().isOk());

        // verifica se a senha foi salva no banco
        List<Password> saved = passwordRepository.findAll();
        assertEquals(1, saved.size());
        assertEquals("github", saved.get(0).getPlataform());
        assertEquals("SenhaF0rte!", saved.get(0).getStrong_password());
        assertEquals(user.getId(), saved.get(0).getUser().getId());
    }
}
