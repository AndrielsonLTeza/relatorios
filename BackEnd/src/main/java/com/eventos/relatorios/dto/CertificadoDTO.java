// CertificadoDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CertificadoDTO {
    private Long id;
    private String usuarioId;
    private Long eventoId;
    private LocalDateTime dataEmissao;
    private String codigoCertificado;
    private EventoDTO evento;
}
