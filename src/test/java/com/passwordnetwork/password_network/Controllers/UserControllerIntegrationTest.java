package com.passwordnetwork.password_network.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.passwordnetwork.password_network.Models.Password;
import com.passwordnetwork.password_network.Models.User;
import com.passwordnetwork.password_network.Repository.PasswordRepository;
import com.passwordnetwork.password_network.Repository.UserRepository;
import com.passwordnetwork.password_network.infra.security.TokenService;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class UserControllerIntegrationTest {

 @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordRepository passwordRepository;

    @MockBean
    TokenService tokenService;

    @BeforeEach
    void setup() {
        passwordRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void userPasswordsDeveRetornarListaDeSenhasDoUsuario() throws Exception {
        String email = "user@test.com";

        // cria usuário
        User user = new User();
        user.setEmail(email);
        user.setPassword("qualquer");
        user = userRepository.save(user);

        // cria senha associada
        Password password = new Password();
        password.setPlataform("github");
        password.setStrong_password("SenhaF0rte!");
        password.setUser(user);
        passwordRepository.save(password);

        // mock do token
        when(tokenService.ValidateToken("fake-token")).thenReturn(email);

        mockMvc.perform(get("/user/passwords")
                .header("Authorization", "Bearer fake-token")
                .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].plataform").value("github"))
            .andExpect(jsonPath("$[0].strong_password").value("SenhaF0rte!"));
    }
}
