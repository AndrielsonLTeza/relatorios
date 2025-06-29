// AvaliacaoDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvaliacaoDTO {
    private Long id;
    private String usuarioId;
    private Long eventoId;
    private Integer notaEvento;
    private Integer notaPalestrante;
    private String comentario;
    private LocalDateTime dataAvaliacao;
    private EventoDTO evento;
}

