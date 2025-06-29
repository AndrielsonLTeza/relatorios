// StatusRelatorio.java (Enum)
package com.eventos.relatorios.model;

public enum StatusRelatorio {
    PENDENTE("Pendente"),
    PROCESSANDO("Processando"),
    CONCLUIDO("Concluído"),
    ERRO("Erro"),
    CANCELADO("Cancelado");
    
    private final String descricao;
    
    StatusRelatorio(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}