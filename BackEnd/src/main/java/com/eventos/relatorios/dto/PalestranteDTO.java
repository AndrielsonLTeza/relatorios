// PalestranteDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;

@Data
public class PalestranteDTO {
    private Long id;
    private String nome;
    private String email;
    private String especialidade;
    private String biografia;
    private String instituicao;
}
