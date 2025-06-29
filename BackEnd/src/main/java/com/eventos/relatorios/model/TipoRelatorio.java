// TipoRelatorio.java (Enum)
package com.eventos.relatorios.model;

public enum TipoRelatorio {
    EVENTOS("Relatório de Eventos"),
    INSCRICOES("Relatório de Inscrições"),
    PALESTRANTES("Relatório de Palestrantes"),
    CONSOLIDADO("Relatório Consolidado");
    
    private final String descricao;
    
    TipoRelatorio(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}