// RelatorioFiltroDTO.java
package com.eventos.relatorios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFiltroDTO {
    
    private String tipoRelatorio;
    
    private String status;
    
    private LocalDate dataInicioSolicitacao;
    
    private LocalDate dataFimSolicitacao;
    
    private String formato;
    
    private Integer page = 0;
    
    private Integer size = 20;
    
    private String ordenarPor = "dataSolicitacao";
    
    private String direcao = "DESC";
}