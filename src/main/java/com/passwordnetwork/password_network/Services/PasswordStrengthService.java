package com.passwordnetwork.password_network.Services;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.passwordnetwork.password_network.DTO.StrengthenPasswordDTO;

@Service
public class PasswordStrengthService {

    private final WebClient webClient;

    public PasswordStrengthService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String strengthenPassword(String weakPassword) {
        // Substitua a URL pela da API real
        String apiUrl = "http://localhost:8081/strongme";
        StrengthenPasswordDTO apiDTO = new StrengthenPasswordDTO(weakPassword,12,true,true,true);

        return webClient.post()
                .uri(apiUrl)
                .bodyValue(apiDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // sync request
    }
}

