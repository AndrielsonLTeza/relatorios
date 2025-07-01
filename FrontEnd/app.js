const API_URL = "http://localhost:8081/api/relatorios/gerados";

$(document).ready(function () {
    loadReports();

    $("#create-form").on("submit", function (e) {
        e.preventDefault();
        const data = {
            tipoRelatorio: $("input[name='tipoRelatorio']").val(),
            formato: $("input[name='formato']").val(),
            nomeRelatorio: $("input[name='nomeRelatorio']").val(),
            usuarioSolicitante: $("input[name='usuarioSolicitante']").val()
        };
        $.ajax({
            url: API_URL,
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function () {
                showMessage("Relatório criado com sucesso!");
                loadReports();
                $("#create-form")[0].reset();
            },
            error: function () {
                showMessage("Erro ao criar relatório.", true);
            }
        });
    });
});

function loadReports() {
    $.get(API_URL, function (data) {
        let html = "";
        data.forEach(function (item) {
            html += `
                <div class="report-item">
                    <strong>ID:</strong> ${item.id}<br>
                    <strong>Nome:</strong> ${item.nomeRelatorio}<br>
                    <strong>Status:</strong> ${item.status}<br>
                    <strong>Progresso:</strong> ${item.progresso}%
                    <button onclick="markAsConcluded(${item.id})">Marcar Concluído</button>
                </div>
            `;
        });
        $("#report-list").html(html);
    });
}

function markAsConcluded(id) {
    $.ajax({
        url: `${API_URL}/${id}/status`,
        type: "PATCH",
        data: {
            status: "CONCLUIDO",
            progresso: 100,
            mensagem: "Finalizado"
        },
        success: function () {
            showMessage("Status atualizado para CONCLUIDO!");
            loadReports();
        },
        error: function () {
            showMessage("Erro ao atualizar status.", true);
        }
    });
}

function showMessage(msg, isError = false) {
    $("#message")
        .text(msg)
        .css("color", isError ? "red" : "green")
        .fadeIn()
        .delay(2000)
        .fadeOut();
}
