// RelatorioService.java
package com.eventos.relatorios.service;

import com.eventos.relatorios.dto.*;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatorioService {

    private final ExternalDataService externalDataService;
    private final PdfGeneratorService pdfGeneratorService;
    private final ExcelGeneratorService excelGeneratorService;
    private final AuthService authService;

    public byte[] gerarRelatorioEventosPdf(String token) throws IOException {
        if (!authService.isTokenValid(token)) {
            throw new SecurityException("Token inválido");
        }

        List<EventoDTO> eventos = externalDataService.getEventos(token);
        log.info("Gerando relatório PDF de eventos. Total: {}", eventos.size());
        
        return pdfGeneratorService.gerarRelatorioEventos(eventos);
    }

    public byte[] gerarRelatorioEventosExcel(String token) throws IOException {
        if (!authService.isTokenValid(token)) {
            throw new SecurityException("Token inválido");
        }

        List<EventoDTO> eventos = externalDataService.getEventos(token);
        log.info("Gerando relatório Excel de eventos. Total: {}", eventos.size());
        
        return excelGeneratorService.gerarRelatorioEventos(eventos);
    }

    public byte[] gerarRelatorioInscricoesPdf(String token) throws IOException {
        if (!authService.isTokenValid(token)) {
            throw new SecurityException("Token inválido");
        }

        List<InscricaoDTO> inscricoes = externalDataService.getInscricoes(token);
        log.info("Gerando relatório PDF de inscrições. Total: {}", inscricoes.size());
        
        return pdfGeneratorService.gerarRelatorioInscricoes(inscricoes);
    }

    public byte[] gerarRelatorioInscricoesExcel(String token) throws IOException {
        if (!authService.isTokenValid(token)) {
            throw new SecurityException("Token inválido");
        }

        List<InscricaoDTO> inscricoes = externalDataService.getInscricoes(token);
        log.info("Gerando relatório Excel de inscrições. Total: {}", inscricoes.size());
        
        return excelGeneratorService.gerarRelatorioInscricoes(inscricoes);
    }

    public byte[] gerarRelatorioPalestrantesPdf(String token) throws IOException {
        if (!authService.isTokenValid(token)) {
            throw new SecurityException("Token inválido");
        }

        List<PalestranteDTO> palestrantes = externalDataService.getPalestrantes(token);
        log.info("Gerando relatório PDF de palestrantes. Total: {}", palestrantes.size());
        
        return pdfGeneratorService.gerarRelatorioPalestrantes(palestrantes);
    }

    public byte[] gerarRelatorioPalestrantesExcel(String token) throws IOException {
        if (!authService.isTokenValid(token)) {
            throw new SecurityException("Token inválido");
        }

        List<PalestranteDTO> palestrantes = externalDataService.getPalestrantes(token);
        log.info("Gerando relatório Excel de palestrantes. Total: {}", palestrantes.size());
        
        return excelGeneratorService.gerarRelatorioPalestrantes(palestrantes);
    }

    public byte[] gerarRelatorioConsolidadoExcel(String token) throws IOException {
        if (!authService.isTokenValid(token)) {
            throw new SecurityException("Token inválido");
        }

        List<EventoDTO> eventos = externalDataService.getEventos(token);
        List<InscricaoDTO> inscricoes = externalDataService.getInscricoes(token);
        List<PalestranteDTO> palestrantes = externalDataService.getPalestrantes(token);
        
        log.info("Gerando relatório consolidado. Eventos: {}, Inscrições: {}, Palestrantes: {}", 
                eventos.size(), inscricoes.size(), palestrantes.size());
        
        return excelGeneratorService.gerarRelatorioConsolidado(eventos, inscricoes, palestrantes);
    }

    public RelatorioEstatisticasDTO gerarEstatisticas(String token) {
        if (!authService.isTokenValid(token)) {
            throw new SecurityException("Token inválido");
        }

        List<EventoDTO> eventos = externalDataService.getEventos(token);
        List<InscricaoDTO> inscricoes = externalDataService.getInscricoes(token);
        List<PalestranteDTO> palestrantes = externalDataService.getPalestrantes(token);
        List<CertificadoDTO> certificados = externalDataService.getCertificados(token);
        List<AvaliacaoDTO> avaliacoes = externalDataService.getAvaliacoes(token);

        RelatorioEstatisticasDTO stats = new RelatorioEstatisticasDTO();
        stats.setTotalEventos(eventos.size());
        stats.setTotalInscricoes(inscricoes.size());
        stats.setTotalPalestrantes(palestrantes.size());
        stats.setTotalCertificados(certificados.size());
        stats.setTotalAvaliacoes(avaliacoes.size());

        // Calcular estatísticas adicionais
        stats.setMediaInscricoesPorEvento(
            eventos.isEmpty() ? 0.0 : (double) inscricoes.size() / eventos.size()
        );

        log.info("Estatísticas geradas: {} eventos, {} inscrições, {} palestrantes", 
                eventos.size(), inscricoes.size(), palestrantes.size());

        return stats;
    }
}