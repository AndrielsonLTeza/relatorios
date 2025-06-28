// InscricaoDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InscricaoDTO {
    private Long id;
    private String usuarioId;
    private Long eventoId;
    private LocalDateTime dataInscricao;
    private String status;
    private EventoDTO evento;
}