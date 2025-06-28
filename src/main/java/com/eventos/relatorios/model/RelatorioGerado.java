package com.eventos.relatorios.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorios_gerados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioGerado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nomeRelatorio;
    
    @Column(nullable = false)
    private String tipoRelatorio; // EVENTOS, INSCRICOES, PALESTRANTES, etc.
    
    @Column(nullable = false)
    private String formato; // PDF, EXCEL
    
    @Column(nullable = false)
    private String usuarioSolicitante;
    
    @Column(nullable = false)
    private LocalDateTime dataGeracao;
    
    @Column(nullable = false)
    private String status; // PROCESSANDO, CONCLUIDO, ERRO
    
    @Column(length = 1000)
    private String parametros; // JSON com filtros aplicados
    
    @Column(length = 500)
    private String caminhoArquivo;
    
    @Column(length = 1000)
    private String mensagemErro;
} 
