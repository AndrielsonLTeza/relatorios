package com.eventos.relatorios.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "relatorios_gerados")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioGerado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tipo_relatorio", nullable = false, length = 50)
    private String tipoRelatorio;
    
    @Column(name = "formato", nullable = false, length = 10)
    private String formato;
    
    @Column(name = "nome_relatorio", nullable = false, length = 200)
    private String nomeRelatorio;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status; // PENDENTE, PROCESSANDO, CONCLUIDO, ERRO, CANCELADO
    
    @Column(name = "progresso")
    private Integer progresso = 0; // 0-100
    
    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao;
    
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
    
    @Column(name = "usuario_solicitante", nullable = false, length = 100)
    private String usuarioSolicitante;
    
    @Column(name = "caminho_arquivo", length = 500)
    private String caminhoArquivo;
    
    @Column(name = "tamanho_arquivo")
    private Long tamanhoArquivo; // em bytes
    
    @Column(name = "mensagem_status", length = 1000)
    private String mensagemStatus;
    
    @ElementCollection
    @CollectionTable(
        name = "relatorio_parametros", 
        joinColumns = @JoinColumn(name = "relatorio_id")
    )
    @MapKeyColumn(name = "chave")
    @Column(name = "valor", length = 1000)
    private Map<String, String> parametros;
    
    @Column(name = "numero_registros")
    private Long numeroRegistros; // quantidade de registros no relatório
    
    @Column(name = "tempo_processamento")
    private Long tempoProcessamento; // tempo em milissegundos
    
    @PrePersist
    protected void onCreate() {
        if (dataSolicitacao == null) {
            dataSolicitacao = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDENTE";
        }
        if (progresso == null) {
            progresso = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        if ("CONCLUIDO".equals(status) && dataConclusao == null) {
            dataConclusao = LocalDateTime.now();
        }
    }
}
