// RelatorioEstatisticasDTO.java
package com.eventos.relatorios.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioEstatisticasDTO {
    private Integer totalEventos;
    private Integer totalInscricoes;
    private Integer totalPalestrantes;
    private Integer totalCertificados;
    private Integer totalAvaliacoes;
    private Double mediaInscricoesPorEvento;
    private Double mediaAvaliacoesPorEvento;
    private Integer eventosAtivos;
    private Integer eventosConcluidos;
    private Integer inscricoesConfirmadas;
    private Integer inscricoesPendentes;
}