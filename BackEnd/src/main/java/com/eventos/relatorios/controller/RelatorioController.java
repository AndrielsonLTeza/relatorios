// RelatorioController.java
package com.eventos.relatorios.controller;

import com.eventos.relatorios.dto.RelatorioEstatisticasDTO;
import com.eventos.relatorios.service.RelatorioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Slf4j
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/eventos/pdf")
    public ResponseEntity<byte[]> gerarRelatorioEventosPdf(
            @RequestHeader("Authorization") String token) {
        try {
            byte[] pdf = relatorioService.gerarRelatorioEventosPdf(token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-eventos.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);
                    
        } catch (SecurityException e) {
            log.error("Erro de autorização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            log.error("Erro ao gerar PDF de eventos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/eventos/excel")
    public ResponseEntity<byte[]> gerarRelatorioEventosExcel(
            @RequestHeader("Authorization") String token) {
        try {
            byte[] excel = relatorioService.gerarRelatorioEventosExcel(token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "relatorio-eventos.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excel);
                    
        } catch (SecurityException e) {
            log.error("Erro de autorização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            log.error("Erro ao gerar Excel de eventos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inscricoes/pdf")
    public ResponseEntity<byte[]> gerarRelatorioInscricoesPdf(
            @RequestHeader("Authorization") String token) {
        try {
            byte[] pdf = relatorioService.gerarRelatorioInscricoesPdf(token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-inscricoes.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);
                    
        } catch (SecurityException e) {
            log.error("Erro de autorização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            log.error("Erro ao gerar PDF de inscrições: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inscricoes/excel")
    public ResponseEntity<byte[]> gerarRelatorioInscricoesExcel(
            @RequestHeader("Authorization") String token) {
        try {
            byte[] excel = relatorioService.gerarRelatorioInscricoesExcel(token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "relatorio-inscricoes.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excel);
                    
        } catch (SecurityException e) {
            log.error("Erro de autorização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            log.error("Erro ao gerar Excel de inscrições: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/palestrantes/pdf")
    public ResponseEntity<byte[]> gerarRelatorioPalestrantesPdf(
            @RequestHeader("Authorization") String token) {
        try {
            byte[] pdf = relatorioService.gerarRelatorioPalestrantesPdf(token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-palestrantes.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);
                    
        } catch (SecurityException e) {
            log.error("Erro de autorização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            log.error("Erro ao gerar PDF de palestrantes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/palestrantes/excel")
    public ResponseEntity<byte[]> gerarRelatorioPalestrantesExcel(
            @RequestHeader("Authorization") String token) {
        try {
            byte[] excel = relatorioService.gerarRelatorioPalestrantesExcel(token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "relatorio-palestrantes.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excel);
                    
        } catch (SecurityException e) {
            log.error("Erro de autorização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            log.error("Erro ao gerar Excel de palestrantes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/consolidado/excel")
    public ResponseEntity<byte[]> gerarRelatorioConsolidadoExcel(
            @RequestHeader("Authorization") String token) {
        try {
            byte[] excel = relatorioService.gerarRelatorioConsolidadoExcel(token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "relatorio-consolidado.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excel);
                    
        } catch (SecurityException e) {
            log.error("Erro de autorização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            log.error("Erro ao gerar relatório consolidado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<RelatorioEstatisticasDTO> gerarEstatisticas(
            @RequestHeader("Authorization") String token) {
        try {
            RelatorioEstatisticasDTO stats = relatorioService.gerarEstatisticas(token);
            return ResponseEntity.ok(stats);
            
        } catch (SecurityException e) {
            log.error("Erro de autorização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Erro ao gerar estatísticas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Serviço de relatórios funcionando!");
    }
}