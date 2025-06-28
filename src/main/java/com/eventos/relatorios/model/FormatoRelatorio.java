// FormatoRelatorio.java (Enum)
package com.eventos.relatorios.model;

public enum FormatoRelatorio {
    PDF("application/pdf", ".pdf"),
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
    
    private final String mimeType;
    private final String extensao;
    
    FormatoRelatorio(String mimeType, String extensao) {
        this.mimeType = mimeType;
        this.extensao = extensao;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public String getExtensao() {
        return extensao;
    }
}