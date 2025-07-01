package com.eventos.relatorios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.eventos.relatorios.model.RelatorioGerado;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.eventos.relatorios.model")
@EnableJpaRepositories("com.eventos.relatorios.repository")
public class RelatoriosApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelatoriosApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}