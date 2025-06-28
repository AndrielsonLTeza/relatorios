// RelatorioController.java
package com.eventos.relatorios.controller;

import com.eventos.relatorios.dto.*;
import com.eventos.relatorios.model.RelatorioGerado;
import com.eventos.relatorios.service.AuthService;
import com.eventos.relatorios.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final AuthService authService;

    @PostMapping("/gerar")
    public ResponseEntity<?> gerarRelatorio(
            @RequestBody RelatorioRequestDTO request,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Validar token
            AuthValidationDTO authResult = authService.validateToken(authHeader);
            if (!authResult.isValid()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido: " + authResult.getMessage());
            }

            // Validar parâmetros
            if (request.getTipoRelatorio() == null || request.getFormato() == null) {
                return ResponseEntity.badRequest()
                    .body("Tipo de relatório e formato são obrigatórios");
            }

            RelatorioResponseDTO response = relatorioService.gerarRelatorio(
                request, authResult.getUserId(), authHeader);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Erro ao gerar relatório: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/meus-relatorios")
    public ResponseEntity<?> listarMeusRelatorios(
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Validar token
            AuthValidationDTO authResult = authService.validateToken(authHeader);
            if (!authResult.isValid()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido: " + authResult.getMessage());
            }

            List<RelatorioResponseDTO> relatorios = relatorioService.listarRelatoriosUsuario(authResult.getUserId());
            return ResponseEntity.ok(relatorios);

        } catch (Exception e) {
            log.error("Erro ao listar relatórios: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterRelatorio(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Validar token
            AuthValidationDTO authResult = authService.validateToken(authHeader);
            if (!authResult.isValid()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido: " + authResult.getMessage());
            }

            Optional<RelatorioGerado> relatorio = relatorioService.buscarRelatorioPorId(id);
            if (relatorio.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar se o relatório pertence ao usuário
            if (!relatorio.get().getUsuarioSolicitante().equals(authResult.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acesso negado ao relatório");
            }

            RelatorioResponseDTO response = mapearParaResponseDTO(relatorio.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Erro ao obter relatório: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> baixarRelatorio(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Validar token
            AuthValidationDTO authResult = authService.validateToken(authHeader);
            if (!authResult.isValid()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido: " + authResult.getMessage());
            }

            byte[] conteudoArquivo = relatorioService.baixarRelatorio(id, authResult.getUserId());
            
            // Determinar tipo de conteúdo e nome do arquivo
            Optional<RelatorioGerado> relatorio = relatorioService.buscarRelatorioPorId(id);
            if (relatorio.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String formato = relatorio.get().getFormato();
            String nomeArquivo = relatorio.get().getNomeRelatorio().replaceAll("[^a-zA-Z0-9\\-_\\.]", "_");
            String extensao = "PDF".equals(formato) ? ".pdf" : ".xlsx";
            MediaType mediaType = "PDF".equals(formato) ? 
                MediaType.APPLICATION_PDF : 
                MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDispositionFormData("attachment", nomeArquivo + extensao);

            return ResponseEntity.ok()
                .headers(headers)
                .body(conteudoArquivo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Relatório ainda não está pronto para download");
        } catch (IOException e) {
            log.error("Erro ao baixar relatório: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao acessar arquivo do relatório");
        } catch (Exception e) {
            log.error("Erro ao baixar relatório: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirRelatorio(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Validar token
            AuthValidationDTO authResult = authService.validateToken(authHeader);
            if (!authResult.isValid()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido: " + authResult.getMessage());
            }

            relatorioService.excluirRelatorio(id, authResult.getUserId());
            return ResponseEntity.ok().body("Relatório excluído com sucesso");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro ao excluir relatório: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> listarTiposRelatorio() {
        List<String> tipos = List.of("EVENTOS", "INSCRICOES", "PALESTRANTES", "CONSOLIDADO");
        return
