$.ajax({
    url: '/api/relatorios/gerados',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({
        tipoRelatorio: 'EVENTOS',
        formato: 'PDF',
        nomeRelatorio: 'Relatório de Eventos',
        usuarioSolicitante: 'admin@teste.com'
    })
});
