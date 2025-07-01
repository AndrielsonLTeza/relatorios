CREATE TABLE IF NOT EXISTS relatorio_gerado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_relatorio VARCHAR(255) NOT NULL,
    tipo_relatorio VARCHAR(100),
    formato VARCHAR(50),
    usuario_solicitante VARCHAR(255),
    data_geracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
