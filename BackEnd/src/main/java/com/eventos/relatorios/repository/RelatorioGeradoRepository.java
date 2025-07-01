// RelatorioGeradoRepository.java
package com.eventos.relatorios.repository;

import com.eventos.relatorios.model.RelatorioGerado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelatorioGeradoRepository extends JpaRepository<RelatorioGerado, Long> {
    
    List<RelatorioGerado> findByUsuarioSolicitanteOrderByDataSolicitacaoDesc(String usuarioSolicitante);

    
    Optional<RelatorioGerado> findByIdAndUsuarioSolicitante(Long id, String usuarioSolicitante);
    
    List<RelatorioGerado> findByStatus(String status);
}