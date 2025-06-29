// RelatorioRepository.java
package com.eventos.relatorios.repository;

import com.eventos.relatorios.model.RelatorioGerado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface RelatorioRepository extends JpaRepository<RelatorioGerado, Long> {
    
    // Buscar relatórios por usuário
    List<RelatorioGerado> findByUsuarioSolicitanteOrderByDataSolicitacaoDesc(String usuarioSolicitante);
    
    // Buscar relatórios por usuário com paginação
    Page<RelatorioGerado> findByUsuarioSolicitante(String usuarioSolicitante, Pageable pageable);
    
    // Buscar relatórios por usuário e tipo
    List<RelatorioGerado> findByUsuarioSolicitanteAndTipoRelatorio(String usuarioSolicitante, String tipoRelatorio);
    
    // Buscar relatórios por status
    List<RelatorioGerado> findByStatus(String status);
    
    // Buscar relatórios pendentes ou processando
    @Query("SELECT r FROM RelatorioGerado r WHERE r.status IN ('PENDENTE', 'PROCESSANDO')")
    List<RelatorioGerado> findRelatoriosEmAndamento();
    
    // Buscar relatórios por período
    @Query("SELECT r FROM RelatorioGerado r WHERE r.usuarioSolicitante = :usuario " +
           "AND r.dataSolicitacao BETWEEN :dataInicio AND :dataFim")
    List<RelatorioGerado> findByUsuarioAndPeriodo(
        @Param("usuario") String usuario,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );
    
    // Buscar relatórios antigos para limpeza
    @Query("SELECT r FROM RelatorioGerado r WHERE r.dataSolicitacao < :dataLimite " +
           "AND r.status = 'CONCLUIDO'")
    List<RelatorioGerado> findRelatoriosAntigos(@Param("dataLimite") LocalDateTime dataLimite);
    
    // Atualizar status do relatório
    @Modifying
    @Query("UPDATE RelatorioGerado r SET r.status = :status, r.progresso = :progresso, " +
           "r.mensagemStatus = :mensagem WHERE r.id = :id")
    int atualizarStatus(
        @Param("id") Long id,
        @Param("status") String status,
        @Param("progresso") Integer progresso,
        @Param("mensagem") String mensagem
    );
    
    // Atualizar progresso
    @Modifying
    @Query("UPDATE RelatorioGerado r SET r.progresso = :progresso WHERE r.id = :id")
    int atualizarProgresso(@Param("id") Long id, @Param("progresso") Integer progresso);
    
    // Finalizar relatório
    @Modifying
    @Query("UPDATE RelatorioGerado r SET r.status = 'CONCLUIDO', r.progresso = 100, " +
           "r.dataConclusao = :dataConclusao, r.caminhoArquivo = :caminhoArquivo, " +
           "r.tamanhoArquivo = :tamanhoArquivo, r.numeroRegistros = :numeroRegistros, " +
           "r.tempoProcessamento = :tempoProcessamento WHERE r.id = :id")
    int finalizarRelatorio(
        @Param("id") Long id,
        @Param("dataConclusao") LocalDateTime dataConclusao,
        @Param("caminhoArquivo") String caminhoArquivo,
        @Param("tamanhoArquivo") Long tamanhoArquivo,
        @Param("numeroRegistros") Long numeroRegistros,
        @Param("tempoProcessamento") Long tempoProcessamento
    );
    
    // Contar relatórios por usuário e status
    @Query("SELECT COUNT(r) FROM RelatorioGerado r WHERE r.usuarioSolicitante = :usuario AND r.status = :status")
    long contarPorUsuarioEStatus(@Param("usuario") String usuario, @Param("status") String status);
    
    // Buscar relatórios com filtros múltiplos
    @Query("SELECT r FROM RelatorioGerado r WHERE " +
           "(:usuario IS NULL OR r.usuarioSolicitante = :usuario) AND " +
           "(:tipo IS NULL OR r.tipoRelatorio = :tipo) AND " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:formato IS NULL OR r.formato = :formato) AND " +
           "(:dataInicio IS NULL OR r.dataSolicitacao >= :dataInicio) AND " +
           "(:dataFim IS NULL OR r.dataSolicitacao <= :dataFim)")
    Page<RelatorioGerado> findComFiltros(
        @Param("usuario") String usuario,
        @Param("tipo") String tipo,
        @Param("status") String status,
        @Param("formato") String formato,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim,
        Pageable pageable
    );
    
    // Verificar se usuário possui relatórios em andamento
   // Substitua a query por este método derivado:
boolean existsByUsuarioSolicitanteAndStatusIn(String usuarioSolicitante, List<String> status);


    // Buscar estatísticas de relatórios
    @Query("SELECT r.tipoRelatorio, COUNT(r), AVG(r.tempoProcessamento) " +
           "FROM RelatorioGerado r WHERE r.status = 'CONCLUIDO' " +
           "GROUP BY r.tipoRelatorio")
    List<Object[]> obterEstatisticasPorTipo();
    
    // Buscar relatório por ID e usuário (para segurança)
    Optional<RelatorioGerado> findByIdAndUsuarioSolicitante(Long id, String usuarioSolicitante);
}
