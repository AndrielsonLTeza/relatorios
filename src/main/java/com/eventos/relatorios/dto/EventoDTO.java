// EventoDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String local;
    private Integer capacidadeMaxima;
    private String status;
    private String categoria;
}