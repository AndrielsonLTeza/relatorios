//RelatorioResponseDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RelatorioResponseDTO {
    private Long id;
    private String nomeRelatorio;
    private String tipoRelatorio;
    private String formato;
    private String status;
    private LocalDateTime dataGeracao;
    private String downloadUrl;
    private String mensagemErro;
}