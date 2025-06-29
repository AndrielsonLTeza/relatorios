const API_URL = "http://localhost:8080/api/relatorios";

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
                    <button onclick="updateStatus(${item.id})">Marcar Concluído</button>
                    <button onclick="deleteReport(${item.id})">Excluir</button>
                </div>
            `;
        });
        $("#report-list").html(html);
    });
}

function updateStatus(id) {
    $.ajax({
        url: `${API_URL}/${id}`,
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify({ status: "CONCLUIDO" }),
        success: function () {
            showMessage("Status atualizado!");
            loadReports();
        },
        error: function () {
            showMessage("Erro ao atualizar.", true);
        }
    });
}

function deleteReport(id) {
    $.ajax({
        url: `${API_URL}/${id}`,
        type: "DELETE",
        success: function () {
            showMessage("Relatório excluído!");
            loadReports();
        },
        error: function () {
            showMessage("Erro ao excluir.", true);
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
