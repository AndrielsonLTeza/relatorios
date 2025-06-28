// ExternalDataService.java
package com.eventos.relatorios.service;

import com.eventos.relatorios.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalDataService {

    private final RestTemplate restTemplate;
    
    @Value("${services.eventos.url}")
    private String eventosServiceUrl;
    
    @Value("${services.inscricoes.url}")
    private String inscricoesServiceUrl;
    
    @Value("${services.palestrantes.url}")
    private String palestrantesServiceUrl;
    
    @Value("${services.certificados.url}")
    private String certificadosServiceUrl;
    
    @Value("${services.avaliacoes.url}")
    private String avaliacoesServiceUrl;

    public List<EventoDTO> getEventos(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<EventoDTO>> response = restTemplate.exchange(
                eventosServiceUrl + "/eventos",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<EventoDTO>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar eventos: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<InscricaoDTO> getInscricoes(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<InscricaoDTO>> response = restTemplate.exchange(
                inscricoesServiceUrl + "/inscricoes",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<InscricaoDTO>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar inscrições: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PalestranteDTO> getPalestrantes(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<PalestranteDTO>> response = restTemplate.exchange(
                palestrantesServiceUrl + "/palestrantes",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<PalestranteDTO>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar palestrantes: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CertificadoDTO> getCertificados(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<CertificadoDTO>> response = restTemplate.exchange(
                certificadosServiceUrl + "/certificados",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CertificadoDTO>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar certificados: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<AvaliacaoDTO> getAvaliacoes(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<AvaliacaoDTO>> response = restTemplate.exchange(
                avaliacoesServiceUrl + "/avaliacoes",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<AvaliacaoDTO>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar avaliações: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
