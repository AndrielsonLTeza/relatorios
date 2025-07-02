package com.eventos.relatorios.controller;

import com.eventos.relatorios.model.RelatorioGerado;
import com.eventos.relatorios.repository.RelatorioGeradoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/relatorios/gerados")
@RequiredArgsConstructor
public class RelatorioGeradoController {

    private final RelatorioGeradoRepository relatorioGeradoRepository;

    /**
     * GET /api/relatorios/gerados
     * Lista todos os relatórios gerados
     */
    @GetMapping
    public ResponseEntity<List<RelatorioGerado>> listarTodos() {
        return ResponseEntity.ok(relatorioGeradoRepository.findAll());
    }

    /**
     * GET /api/relatorios/gerados/usuario?usuario=xxx
     * Lista relatórios por usuário com paginação
     */
    @GetMapping("/usuario")
    public ResponseEntity<Page<RelatorioGerado>> listarPorUsuario(
            @RequestParam String usuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<RelatorioGerado> result = relatorioGeradoRepository.findByUsuarioSolicitante(
                usuario, PageRequest.of(page, size)
        );
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/relatorios/gerados/em-andamento
     * Lista relatórios em andamento (PENDENTE, PROCESSANDO)
     */
    @GetMapping("/em-andamento")
    public ResponseEntity<List<RelatorioGerado>> listarEmAndamento() {
        return ResponseEntity.ok(relatorioGeradoRepository.findRelatoriosEmAndamento());
    }

    /**
     * GET /api/relatorios/gerados/{id}
     * Busca relatório por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RelatorioGerado> buscarPorId(@PathVariable Long id) {
        Optional<RelatorioGerado> encontrado = relatorioGeradoRepository.findById(id);
        return encontrado.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/relatorios/gerados
     * Cria novo relatório gerado
     */
    @PostMapping
    public ResponseEntity<RelatorioGerado> criar(@RequestBody RelatorioGerado relatorio) {
        relatorio.setId(null);
        relatorio.setStatus("PENDENTE");
        relatorio.setProgresso(0);
        relatorio.setDataSolicitacao(LocalDateTime.now());
        relatorio.setMensagemStatus("Aguardando processamento");

        RelatorioGerado salvo = relatorioGeradoRepository.save(relatorio);
        return ResponseEntity.ok(salvo);
    }

    /**
     * PATCH /api/relatorios/gerados/{id}/status
     * Atualiza status + progresso + mensagem
     */
    @Transactional
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(defaultValue = "0") Integer progresso,
            @RequestParam(required = false) String mensagem) {

        int atualizados = relatorioGeradoRepository.atualizarStatus(id, status, progresso, mensagem);
        return atualizados > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * PATCH /api/relatorios/gerados/{id}/progresso
     * Atualiza apenas o progresso
     */
    @Transactional
    @PatchMapping("/{id}/progresso")
    public ResponseEntity<Void> atualizarProgresso(
            @PathVariable Long id,
            @RequestParam Integer progresso) {

        int atualizados = relatorioGeradoRepository.atualizarProgresso(id, progresso);
        return atualizados > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * PATCH /api/relatorios/gerados/{id}/finalizar
     * Marca como CONCLUIDO e salva dados do arquivo
     */
    @Transactional
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarRelatorio(
            @PathVariable Long id,
            @RequestParam String caminhoArquivo,
            @RequestParam Long tamanhoArquivo,
            @RequestParam Long numeroRegistros,
            @RequestParam Long tempoProcessamento) {

        int atualizados = relatorioGeradoRepository.finalizarRelatorio(
                id,
                LocalDateTime.now(),
                caminhoArquivo,
                tamanhoArquivo,
                numeroRegistros,
                tempoProcessamento
        );
        return atualizados > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * GET /api/relatorios/gerados/filtros
     * Busca avançada com filtros e paginação
     */
    @GetMapping("/filtros")
    public ResponseEntity<Page<RelatorioGerado>> buscarComFiltros(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String formato,
            @RequestParam(required = false) LocalDateTime dataInicio,
            @RequestParam(required = false) LocalDateTime dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<RelatorioGerado> resultado = relatorioGeradoRepository.findComFiltros(
                usuario, tipo, status, formato, dataInicio, dataFim, PageRequest.of(page, size)
        );
        return ResponseEntity.ok(resultado);
    }
}