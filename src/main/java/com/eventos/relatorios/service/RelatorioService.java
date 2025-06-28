// RelatorioService.java
package com.eventos.relatorios.service;

import com.eventos.relatorios.dto.*;
import com.eventos.relatorios.model.RelatorioGerado;
import com.eventos.relatorios.repository.RelatorioGeradoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatorioService {

    private final RelatorioGeradoRepository relatorioRepository;
    private final ExternalDataService externalDataService;
    private final PdfGeneratorService pdfGeneratorService;
    private final ExcelGeneratorService excelGeneratorService;
    private final ObjectMapper objectMapper;

    private static final String REPORTS_DIRECTORY = "reports/";

    @Transactional
    public RelatorioResponseDTO gerarRelatorio(RelatorioRequestDTO request, String usuarioId, String token) {
        // Criar registro inicial
        RelatorioGerado relatorio = new RelatorioGerado();
        relatorio.setNomeRelatorio(gerarNomeRelatorio(request));
        relatorio.setTipoRelatorio(request.getTipoRelatorio());
        relatorio.setFormato(request.getFormato());
        relatorio.setUsuarioSolicitante(usuarioId);
        relatorio.setDataGeracao(LocalDateTime.now());
        relatorio.setStatus("PROCESSANDO");
        
        try {
            relatorio.setParametros(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar parâmetros: {}", e.getMessage());
            relatorio.setParametros("{}");
        }

        relatorio = relatorioRepository.save(relatorio);

        // Processar relatório de forma assíncrona
        CompletableFuture.runAsync(() -> processarRelatorio(relatorio, request, token));

        return mapearParaResponseDTO(relatorio);
    }

    private void processarRelatorio(RelatorioGerado relatorio, RelatorioRequestDTO request, String token) {
        try {
            byte[] conteudoRelatorio = null;
            String extensao = "";

            switch (request.getTipoRelatorio().toUpperCase()) {
                case "EVENTOS":
                    conteudoRelatorio = gerarRelatorioEventos(request, token);
                    break;
                case "INSCRICOES":
                    conteudoRelatorio = gerarRelatorioInscricoes(request, token);
                    break;
                case "PALESTRANTES":
                    conteudoRelatorio = gerarRelatorioPalestrantes(request, token);
                    break;
                case "CONSOLIDADO":
                    conteudoRelatorio = gerarRelatorioConsolidado(request, token);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de relatório não suportado: " + request.getTipoRelatorio());
            }

            if (conteudoRelatorio != null) {
                extensao = "PDF".equals(request.getFormato()) ? ".pdf" : ".xlsx";
                String nomeArquivo = "relatorio_" + relatorio.getId() + "_" + UUID.randomUUID().toString() + extensao;
                String caminhoArquivo = salvarArquivo(conteudoRelatorio, nomeArquivo);

                relatorio.setCaminhoArquivo(caminhoArquivo);
                relatorio.setStatus("CONCLUIDO");
            } else {
                relatorio.setStatus("ERRO");
                relatorio.setMensagemErro("Não foi possível gerar o conteúdo do relatório");
            }

        } catch (Exception e) {
            log.error("Erro ao processar relatório {}: {}", relatorio.getId(), e.getMessage(), e);
            relatorio.setStatus("ERRO");
            relatorio.setMensagemErro(e.getMessage());
        }

        relatorioRepository.save(relatorio);
    }

    private byte[] gerarRelatorioEventos(RelatorioRequestDTO request, String token) throws IOException {
        List<EventoDTO> eventos = externalDataService.getEventos(token);
        
        if ("PDF".equals(request.getFormato())) {
            return pdfGeneratorService.gerarRelatorioEventos(eventos);
        } else {
            return excelGeneratorService.gerarRelatorioEventos(eventos);
        }
    }

    private byte[] gerarRelatorioInscricoes(RelatorioRequestDTO request, String token) throws IOException {
        List<InscricaoDTO> inscricoes = externalDataService.getInscricoes(token);
        
        if ("PDF".equals(request.getFormato())) {
            return pdfGeneratorService.gerarRelatorioInscricoes(inscricoes);
        } else {
            return excelGeneratorService.gerarRelatorioInscricoes(inscricoes);
        }
    }

    private byte[] gerarRelatorioPalestrantes(RelatorioRequestDTO request, String token) throws IOException {
        List<PalestranteDTO> palestrantes = externalDataService.getPalestrantes(token);
        
        if ("PDF".equals(request.getFormato())) {
            return pdfGeneratorService.gerarRelatorioPalestrantes(palestrantes);
        } else {
            return excelGeneratorService.gerarRelatorioPalestrantes(palestrantes);
        }
    }

    private byte[] gerarRelatorioConsolidado(RelatorioRequestDTO request, String token) throws IOException {
        List<EventoDTO> eventos = externalDataService.getEventos(token);
        List<InscricaoDTO> inscricoes = externalDataService.getInscricoes(token);
        List<PalestranteDTO> palestrantes = externalDataService.getPalestrantes(token);

        if ("EXCEL".equals(request.getFormato())) {
            return excelGeneratorService.gerarRelatorioConsolidado(eventos, inscricoes, palestrantes);
        } else {
            // Para PDF consolidado, gerar eventos por padrão
            return pdfGeneratorService.gerarRelatorioEventos(eventos);
        }
    }

    private String salvarArquivo(byte[] conteudo, String nomeArquivo) throws IOException {
        Path diretorioReports = Paths.get(REPORTS_DIRECTORY);
        if (!Files.exists(diretorioReports)) {
            Files.createDirectories(diretorioReports);
        }

        Path caminhoArquivo = diretorioReports.resolve(nomeArquivo);
        Files.write(caminhoArquivo, conteudo);
        
        return caminhoArquivo.toString();
    }

    private String gerarNomeRelatorio(RelatorioRequestDTO request) {
        return String.format("Relatório %s (%s)", 
            request.getTipoRelatorio().toLowerCase(), 
            request.getFormato());
    }

    public List<RelatorioResponseDTO> listarRelatoriosUsuario(String usuarioId) {
        List<RelatorioGerado> relatorios = relatorioRepository.findByUsuarioSolicitanteOrderByDataGeracaoDesc(usuarioId);
        return relatorios.stream()
                .map(this::mapearParaResponseDTO)
                .toList();
    }

    public Optional<RelatorioGerado> buscarRelatorioPorId(Long id) {
        return relatorioRepository.findById(id);
    }

    public byte[] baixarRelatorio(Long id, String usuarioId) throws IOException {
        Optional<RelatorioGerado> relatorioOpt = relatorioRepository.findByIdAndUsuarioSolicitante(id, usuarioId);
        
        if (relatorioOpt.isEmpty()) {
            throw new IllegalArgumentException("Relatório não encontrado ou não pertence ao usuário");
        }

        RelatorioGerado relatorio = relatorioOpt.get();
        
        if (!"CONCLUIDO".equals(relatorio.getStatus())) {
            throw new IllegalStateException("Relatório ainda não foi concluído");
        }

        Path caminhoArquivo = Paths.get(relatorio.getCaminhoArquivo());
        if (!Files.exists(caminhoArquivo)) {
            throw new IOException("Arquivo do relatório não encontrado");
        }

        return Files.readAllBytes(caminhoArquivo);
    }

    @Transactional
    public void excluirRelatorio(Long id, String usuarioId) throws IOException {
        Optional<RelatorioGerado> relatorioOpt = relatorioRepository.findByIdAndUsuarioSolicitante(id, usuarioId);
        
        if (relatorioOpt.isEmpty()) {
            throw new IllegalArgumentException("Relatório não encontrado ou não pertence ao usuário");
        }

        RelatorioGerado relatorio = relatorioOpt.get();
        
        // Excluir arquivo físico se existir
        if (relatorio.getCaminhoArquivo() != null) {
            Path caminhoArquivo = Paths.get(relatorio.getCaminhoArquivo());
            if (Files.exists(caminhoArquivo)) {
                Files.delete(caminhoArquivo);
            }
        }

        // Excluir registro do banco
        relatorioRepository.delete(relatorio);
    }

    private RelatorioResponseDTO mapearParaResponseDTO(RelatorioGerado relatorio) {
        RelatorioResponseDTO dto = new RelatorioResponseDTO();
        dto.setId(relatorio.getId());
        dto.setNomeRelatorio(relatorio.getNomeRelatorio());
        dto.setTipoRelatorio(relatorio.getTipoRelatorio());
        dto.setFormato(relatorio.getFormato());
        dto.setStatus(relatorio.getStatus());
        dto.setDataGeracao(relatorio.getDataGeracao());
        dto.setMensagemErro(relatorio.getMensagemErro());
        
        if ("CONCLUIDO".equals(relatorio.getStatus())) {
            dto.setDownloadUrl("/api/relatorios/" + relatorio.getId() + "/download");
        }
        
        return dto;
    }
}
