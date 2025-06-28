// RelatorioService.java
package com.eventos.relatorios.service;

import com.eventos.relatorios.dto.*;
import com.eventos.relatorios.model.RelatorioGerado;
import com.eventos.relatorios.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final GeracaoRelatorioService geracaoRelatorioService;
    private final ArquivoService arquivoService;
    private final EventoService eventoService; // Para buscar dados de eventos
    
    @Value("${relatorios.diretorio.base:/tmp/relatorios}")
    private String diretorioBase;
    
    @Value("${relatorios.limite.usuario:5}")
    private int limiteRelatoriosPorUsuario;

    public RelatorioResponseDTO gerarRelatorio(RelatorioRequestDTO request, String userId, String authHeader) {
        // Validar limite de relatórios simultâneos
        validarLimiteRelatorios(userId);
        
        // Criar entidade
        RelatorioGerado relatorio = criarRelatorioEntidade(request, userId);
        relatorio = relatorioRepository.save(relatorio);
        
        // Iniciar geração assíncrona
        iniciarGeracaoAssincrona(relatorio, authHeader);
        
        return mapearParaResponseDTO(relatorio);
    }

    @Async
    public CompletableFuture<Void> iniciarGeracaoAssincrona(RelatorioGerado relatorio, String authHeader) {
        try {
            log.info("Iniciando geração do relatório ID: {}", relatorio.getId());
            
            // Atualizar status para PROCESSANDO
            atualizarStatusRelatorio(relatorio.getId(), "PROCESSANDO", 10, "Iniciando processamento...");
            
            // Gerar relatório baseado no tipo
            byte[] conteudoRelatorio = gerarConteudoRelatorio(relatorio, authHeader);
            
            // Salvar arquivo
            String caminhoArquivo = salvarArquivoRelatorio(relatorio, conteudoRelatorio);
            
            // Finalizar relatório
            finalizarRelatorio(relatorio.getId(), caminhoArquivo, conteudoRelatorio.length);
            
            log.info("Relatório ID: {} gerado com sucesso", relatorio.getId());
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório ID: {}", relatorio.getId(), e);
            atualizarStatusRelatorio(relatorio.getId(), "ERRO", 0, "Erro na geração: " + e.getMessage());
        }
        
        return CompletableFuture.completedFuture(null);
    }

    private byte[] gerarConteudoRelatorio(RelatorioGerado relatorio, String authHeader) throws Exception {
        atualizarStatusRelatorio(relatorio.getId(), "PROCESSANDO", 30, "Coletando dados...");
        
        // Buscar dados baseado no tipo de relatório
        Map<String, Object> dados = coletarDadosRelatorio(relatorio, authHeader);
        
        atualizarStatusRelatorio(relatorio.getId(), "PROCESSANDO", 60, "Processando dados...");
        
        // Gerar arquivo baseado no formato
        byte[] conteudo;
        if ("PDF".equals(relatorio.getFormato())) {
            conteudo = geracaoRelatorioService.gerarPDF(relatorio.getTipoRelatorio(), dados);
        } else if ("EXCEL".equals(relatorio.getFormato())) {
            conteudo = geracaoRelatorioService.gerarExcel(relatorio.getTipoRelatorio(), dados);
        } else {
            throw new IllegalArgumentException("Formato não suportado: " + relatorio.getFormato());
        }
        
        atualizarStatusRelatorio(relatorio.getId(), "PROCESSANDO", 90, "Finalizando...");
        
        return conteudo;
    }

    private Map<String, Object> coletarDadosRelatorio(RelatorioGerado relatorio, String authHeader) {
        switch (relatorio.getTipoRelatorio()) {
            case "EVENTOS":
                return eventoService.buscarDadosEventos(relatorio.getParametros(), authHeader);
            case "INSCRICOES":
                return eventoService.buscarDadosInscricoes(relatorio.getParametros(), authHeader);
            case "PALESTRANTES":
                return eventoService.buscarDadosPalestrantes(relatorio.getParametros(), authHeader);
            case "CONSOLIDADO":
                return eventoService.buscarDadosConsolidados(relatorio.getParametros(), authHeader);
            default:
                throw new IllegalArgumentException("Tipo de relatório não suportado: " + relatorio.getTipoRelatorio());
        }
    }

    private String salvarArquivoRelatorio(RelatorioGerado relatorio, byte[] conteudo) throws IOException {
        String nomeArquivo = gerarNomeArquivo(relatorio);
        String caminhoCompleto = diretorioBase + "/" + relatorio.getUsuarioSolicitante() + "/" + nomeArquivo;
        
        Path caminho = Paths.get(caminhoCompleto);
        Files.createDirectories(caminho.getParent());
        Files.write(caminho, conteudo);
        
        return caminhoCompleto;
    }

    private String gerarNomeArquivo(RelatorioGerado relatorio) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extensao = "PDF".equals(relatorio.getFormato()) ? ".pdf" : ".xlsx";
        return String.format("%s_%s_%s%s", 
            relatorio.getTipoRelatorio().toLowerCase(),
            relatorio.getId(),
            timestamp,
            extensao);
    }

    public List<RelatorioResponseDTO> listarRelatoriosUsuario(String userId) {
        List<RelatorioGerado> relatorios = relatorioRepository
            .findByUsuarioSolicitanteOrderByDataSolicitacaoDesc(userId);
        
        return relatorios.stream()
            .map(this::mapearParaResponseDTO)
            .collect(Collectors.toList());
    }

    public Page<RelatorioResponseDTO> listarRelatoriosUsuarioComPaginacao(String userId, RelatorioFiltroDTO filtro) {
        Pageable pageable = PageRequest.of(
            filtro.getPage(),
            filtro.getSize(),
            Sort.by(Sort.Direction.fromString(filtro.getDirecao()), filtro.getOrdenarPor())
        );
        
        Page<RelatorioGerado> relatorios = relatorioRepository.findComFiltros(
            userId,
            filtro.getTipoRelatorio(),
            filtro.getStatus(),
            filtro.getFormato(),
            filtro.getDataInicioSolicitacao() != null ? filtro.getDataInicioSolicitacao().atStartOfDay() : null,
            filtro.getDataFimSolicitacao() != null ? filtro.getDataFimSolicitacao().atTime(23, 59, 59) : null,
            pageable
        );
        
        return relatorios.map(this::mapearParaResponseDTO);
    }

    public Optional<RelatorioGerado> buscarRelatorioPorId(Long id) {
        return relatorioRepository.findById(id);
    }

    public byte[] baixarRelatorio(Long id, String userId) throws IOException {
        RelatorioGerado relatorio = relatorioRepository.findByIdAndUsuarioSolicitante(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Relatório não encontrado"));
        
        if (!"CONCLUIDO".equals(relatorio.getStatus())) {
            throw new IllegalStateException("Relatório ainda não está pronto para download");
        }
        
        Path caminho = Paths.get(relatorio.getCaminhoArquivo());
        if (!Files.exists(caminho)) {
            throw new IOException("Arquivo do relatório não encontrado");
        }
        
        return Files.readAllBytes(caminho);
    }

    public void excluirRelatorio(Long id, String userId) {
        RelatorioGerado relatorio = relatorioRepository.findByIdAndUsuarioSolicitante(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Relatório não encontrado"));
        
        // Excluir arquivo físico se existir
        if (relatorio.getCaminhoArquivo() != null) {
            try {
                Files.deleteIfExists(Paths.get(relatorio.getCaminhoArquivo()));
            } catch (IOException e) {
                log.warn("Erro ao excluir arquivo físico do relatório ID: {}", id, e);
            }
        }
        
        relatorioRepository.delete(relatorio);
    }

    public void cancelarRelatorio(Long id, String userId) {
        RelatorioGerado relatorio = relatorioRepository.findByIdAndUsuarioSolicitante(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Relatório não encontrado"));
        
        if ("CONCLUIDO".equals(relatorio.getStatus()) || "CANCELADO".equals(relatorio.getStatus())) {
            throw new IllegalStateException("Não é possível cancelar relatório com status: " + relatorio.getStatus());
        }
        
        atualizarStatusRelatorio(id, "CANCELADO", relatorio.getProgresso(), "Cancelado pelo usuário");
    }

    private void validarLimiteRelatorios(String userId) {
        long relatoriosEmAndamento = relatorioRepository.contarPorUsuarioEStatus(userId, "PENDENTE") +
                                    relatorioRepository.contarPorUsuarioEStatus(userId, "PROCESSANDO");
        
        if (relatoriosEmAndamento >= limiteRelatoriosPorUsuario) {
            throw new IllegalStateException("Limite de relatórios simultâneos atingido. Aguarde a conclusão de relatórios em andamento.");
        }
    }

    private RelatorioGerado criarRelatorioEntidade(RelatorioRequestDTO request, String userId) {
        String nomeRelatorio = request.getNomeRelatorio();
        if (nomeRelatorio == null || nomeRelatorio.trim().isEmpty()) {
            nomeRelatorio = String.format("Relatório %s - %s", 
                request.getTipoRelatorio(), 
                LocalDateTime.now().toString());
        }
        
        return RelatorioGerado.builder()
            .tipoRelatorio(request.getTipoRelatorio())
            .formato(request.getFormato())
            .nomeRelatorio(nomeRelatorio)
            .usuarioSolicitante(userId)
            .status("PENDENTE")
            .progresso(0)
            .parametros(request.getParametros() != null ? 
                request.getParametros().entrySet().stream()
                    .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        entry -> String.valueOf(entry.getValue())
                    )) : null)
            .build();
    }

    private void atualizarStatusRelatorio(Long id, String status, Integer progresso, String mensagem) {
        relatorioRepository.atualizarStatus(id, status, progresso, mensagem);
    }

    private void finalizarRelatorio(Long id, String caminhoArquivo, long tamanhoArquivo) {
        relatorioRepository.finalizarRelatorio(
            id, 
            LocalDateTime.now(), 
            caminhoArquivo, 
            tamanhoArquivo, 
            0L, // numeroRegistros - será implementado conforme necessário
            0L  // tempoProcessamento - será implementado conforme necessário
        );
    }

    private RelatorioResponseDTO mapearParaResponseDTO(RelatorioGerado relatorio) {
        return RelatorioResponseDTO.builder()
            .id(relatorio.getId())
            .tipoRelatorio(relatorio.getTipoRelatorio())
            .formato(relatorio.getFormato())
            .nomeRelatorio(relatorio.getNomeRelatorio())
            .status(relatorio.getStatus())
            .progresso(relatorio.getProgresso())
            .dataSolicitacao(relatorio.getDataSolicitacao())
            .dataConclusao(relatorio.getDataConclusao())
            .parametros(relatorio.getParametros() != null ?
                relatorio.getParametros().entrySet().stream()
                    .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        entry -> entry.getValue()
                    )) : null)
            .tamanhoArquivo(relatorio.getTamanhoArquivo())
            .numeroRegistros(relatorio.getNumeroRegistros())
            .tempoProcessamento(relatorio.getTempoProcessamento())
            .mensagemStatus(relatorio.getMensagemStatus())
            .caminhoArquivo(relatorio.getCaminhoArquivo())
            .build();
    }

    // Método para buscar estatísticas de relatórios do usuário
    public RelatorioEstatisticasDTO obterEstatisticasUsuario(String userId) {
        return RelatorioEstatisticasDTO.builder()
            .totalRelatorios(relatorioRepository.contarPorUsuario(userId))
            .relatoriosConcluidos(relatorioRepository.contarPorUsuarioEStatus(userId, "CONCLUIDO"))
            .relatoriosEmAndamento(relatorioRepository.contarPorUsuarioEStatus(userId, "PROCESSANDO"))
            .relatoriosPendentes(relatorioRepository.contarPorUsuarioEStatus(userId, "PENDENTE"))
            .relatoriosComErro(relatorioRepository.contarPorUsuarioEStatus(userId, "ERRO"))
            .build();
    }

    // Método para limpeza de relatórios antigos (pode ser chamado por scheduler)
    @Transactional
    public void limparRelatoriosAntigos(int diasParaExcluir) {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(diasParaExcluir);
        List<RelatorioGerado> relatoriosAntigos = relatorioRepository
            .findByDataSolicitacaoBeforeAndStatus(dataLimite, "CONCLUIDO");
        
        for (RelatorioGerado relatorio : relatoriosAntigos) {
            try {
                // Excluir arquivo físico
                if (relatorio.getCaminhoArquivo() != null) {
                    Files.deleteIfExists(Paths.get(relatorio.getCaminhoArquivo()));
                }
                // Excluir registro do banco
                relatorioRepository.delete(relatorio);
                
                log.info("Relatório antigo excluído: ID {}", relatorio.getId());
            } catch (Exception e) {
                log.error("Erro ao excluir relatório antigo ID: {}", relatorio.getId(), e);
            }
        }
    }

    // Método para reprocessar relatório com erro
    public RelatorioResponseDTO reprocessarRelatorio(Long id, String userId, String authHeader) {
        RelatorioGerado relatorio = relatorioRepository.findByIdAndUsuarioSolicitante(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Relatório não encontrado"));
        
        if (!"ERRO".equals(relatorio.getStatus()) && !"CANCELADO".equals(relatorio.getStatus())) {
            throw new IllegalStateException("Apenas relatórios com erro ou cancelados podem ser reprocessados");
        }
        
        // Resetar status e progresso
        atualizarStatusRelatorio(id, "PENDENTE", 0, "Aguardando reprocessamento...");
        
        // Reiniciar geração assíncrona
        iniciarGeracaoAssincrona(relatorio, authHeader);
        
        return mapearParaResponseDTO(relatorio);
    }
}