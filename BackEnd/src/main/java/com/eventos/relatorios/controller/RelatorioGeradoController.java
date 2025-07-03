package com.eventos.relatorios.controller;

import com.eventos.relatorios.model.RelatorioGerado;
import com.eventos.relatorios.repository.RelatorioGeradoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioGeradoController {

    private final RelatorioGeradoRepository repository;

    public RelatorioGeradoController(RelatorioGeradoRepository repository) {
        this.repository = repository;
    }

    /**
     * Endpoint para obter todos os relatórios gerados do banco de dados
     * Retorna a lista de RelatorioGerado em formato JSON
     */
    @GetMapping
    public List<RelatorioGerado> listarTodos() {
        return repository.findAll();
    }

    /**
     * Endpoint para buscar um relatório específico por ID
     * Retorna 200 OK com o RelatorioGerado em JSON ou 404 Not Found se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<RelatorioGerado> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                         .map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
}
