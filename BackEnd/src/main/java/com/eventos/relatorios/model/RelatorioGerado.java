package com.eventos.relatorios.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorio_gerado")
public class RelatorioGerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_relatorio", nullable = false)
    private String nomeRelatorio;

    @Column(name = "tipo_relatorio", nullable = false)
    private String tipoRelatorio;

    @Column(nullable = false)
    private String formato;

    @Column(name = "usuario_solicitante", nullable = false)
    private String usuarioSolicitante;

    @Column(name = "data_geracao", nullable = false)
    private LocalDateTime dataGeracao;

    public RelatorioGerado() {
    }

    public RelatorioGerado(String nomeRelatorio,
String tipoRelatorio,
String formato,
String usuarioSolicitante,
LocalDateTime dataGeracao) {
        this.nomeRelatorio = nomeRelatorio;
        this.tipoRelatorio = tipoRelatorio;
        this.formato = formato;
        this.usuarioSolicitante = usuarioSolicitante;
        this.dataGeracao = dataGeracao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
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
    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }
    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }
}
