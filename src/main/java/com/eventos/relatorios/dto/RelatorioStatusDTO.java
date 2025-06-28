// RelatorioStatusDTO.java
package com.eventos.relatorios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioStatusDTO {
    
    private Long id;
    
    private String status;
    
    private Integer progresso;
    
    private String mensagem;
    
    private String tempoEstimado; // tempo estimado para conclusão
}