// AuthService.java
package com.eventos.relatorios.service;

import com.eventos.relatorios.dto.AuthValidationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RestTemplate restTemplate;
    
    @Value("${services.auth.url}")
    private String authServiceUrl;

    public AuthValidationDTO validateToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<AuthValidationDTO> response = restTemplate.exchange(
                authServiceUrl + "/auth/validate-token",
                HttpMethod.GET,
                entity,
                AuthValidationDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao validar token: {}", e.getMessage());
            AuthValidationDTO invalidResponse = new AuthValidationDTO();
            invalidResponse.setValid(false);
            invalidResponse.setMessage("Token inválido");
            return invalidResponse;
        }
    }
}

