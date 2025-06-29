// RelatorioRequestDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class RelatorioRequestDTO {
    private String tipoRelatorio;
    private String formato; // PDF ou EXCEL
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<Long> eventosIds;
    private String categoria;
    private String status;
}