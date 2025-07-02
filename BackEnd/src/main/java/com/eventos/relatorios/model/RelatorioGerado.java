// RelatorioGerado.java
package com.eventos.relatorios.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorio_gerado")
public class RelatorioGerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String formato;

    @Column(nullable = false)
    private String usuarioSolicitante;

    @Column(nullable = false)
    private String status;

    @Column
    private Integer progresso;

    @Column
    private String mensagemStatus;

    @Column
    private LocalDateTime dataSolicitacao;

    @Column
    private LocalDateTime dataGeracao;

    @Column
    private String arquivoPath;

    @Column
    private Long tamanhoArquivo;

    @Column
    private Long numeroRegistros;

    @Column
    private Long tempoProcessamento;

    // Construtores
    public RelatorioGerado() {}

    public RelatorioGerado(String nome, String tipo, String formato, String usuarioSolicitante,
                            String status, Integer progresso, String mensagemStatus,
                            LocalDateTime dataSolicitacao, LocalDateTime dataGeracao,
                            String arquivoPath, Long tamanhoArquivo, Long numeroRegistros, Long tempoProcessamento) {
        this.nome = nome;
        this.tipo = tipo;
        this.formato = formato;
        this.usuarioSolicitante = usuarioSolicitante;
        this.status = status;
        this.progresso = progresso;
        this.mensagemStatus = mensagemStatus;
        this.dataSolicitacao = dataSolicitacao;
        this.dataGeracao = dataGeracao;
        this.arquivoPath = arquivoPath;
        this.tamanhoArquivo = tamanhoArquivo;
        this.numeroRegistros = numeroRegistros;
        this.tempoProcessamento = tempoProcessamento;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(String usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProgresso() {
        return progresso;
    }

    public void setProgresso(Integer progresso) {
        this.progresso = progresso;
    }

    public String getMensagemStatus() {
        return mensagemStatus;
    }

    public void setMensagemStatus(String mensagemStatus) {
        this.mensagemStatus = mensagemStatus;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public String getArquivoPath() {
        return arquivoPath;
    }

    public void setArquivoPath(String arquivoPath) {
        this.arquivoPath = arquivoPath;
    }

    public Long getTamanhoArquivo() {
        return tamanhoArquivo;
    }

    public void setTamanhoArquivo(Long tamanhoArquivo) {
        this.tamanhoArquivo = tamanhoArquivo;
    }

    public Long getNumeroRegistros() {
        return numeroRegistros;
    }

    public void setNumeroRegistros(Long numeroRegistros) {
        this.numeroRegistros = numeroRegistros;
    }

    public Long getTempoProcessamento() {
        return tempoProcessamento;
    }

    public void setTempoProcessamento(Long tempoProcessamento) {
        this.tempoProcessamento = tempoProcessamento;
    }
}
