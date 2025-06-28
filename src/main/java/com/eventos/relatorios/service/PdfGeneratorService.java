// PdfGeneratorService.java
package com.eventos.relatorios.service;

import com.eventos.relatorios.dto.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class PdfGeneratorService {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] gerarRelatorioEventos(List<EventoDTO> eventos) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            PdfFont font = PdfFontFactory.createFont();
            
            // Título
            Paragraph titulo = new Paragraph("Relatório de Eventos")
                    .setFont(font)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();
            document.add(titulo);
            
            document.add(new Paragraph("\n"));

            // Tabela de eventos
            Table table = new Table(5);
            table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Nome").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Data Início").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Local").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Status").setBold()));

            for (EventoDTO evento : eventos) {
                table.addCell(new Cell().add(new Paragraph(evento.getId().toString())));
                table.addCell(new Cell().add(new Paragraph(evento.getNome())));
                table.addCell(new Cell().add(new Paragraph(
                    evento.getDataInicio() != null ? 
                    evento.getDataInicio().format(dateFormatter) : "N/A")));
                table.addCell(new Cell().add(new Paragraph(evento.getLocal() != null ? evento.getLocal() : "N/A")));
                table.addCell(new Cell().add(new Paragraph(evento.getStatus() != null ? evento.getStatus() : "N/A")));
            }

            document.add(table);
            
            // Resumo
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de eventos: " + eventos.size()).setBold());
            
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    public byte[] gerarRelatorioInscricoes(List<InscricaoDTO> inscricoes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            PdfFont font = PdfFontFactory.createFont();
            
            // Título
            Paragraph titulo = new Paragraph("Relatório de Inscrições")
                    .setFont(font)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();
            document.add(titulo);
            
            document.add(new Paragraph("\n"));

            // Tabela de inscrições
            Table table = new Table(4);
            table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Usuário").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Evento").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Data Inscrição").setBold()));

            for (InscricaoDTO inscricao : inscricoes) {
                table.addCell(new Cell().add(new Paragraph(inscricao.getId().toString())));
                table.addCell(new Cell().add(new Paragraph(inscricao.getUsuarioId())));
                table.addCell(new Cell().add(new Paragraph(
                    inscricao.getEvento() != null ? inscricao.getEvento().getNome() : "N/A")));
                table.addCell(new Cell().add(new Paragraph(
                    inscricao.getDataInscricao() != null ? 
                    inscricao.getDataInscricao().format(dateFormatter) : "N/A")));
            }

            document.add(table);
            
            // Resumo
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de inscrições: " + inscricoes.size()).setBold());
            
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    public byte[] gerarRelatorioPalestrantes(List<PalestranteDTO> palestrantes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            PdfFont font = PdfFontFactory.createFont();
            
            // Título
            Paragraph titulo = new Paragraph("Relatório de Palestrantes")
                    .setFont(font)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();
            document.add(titulo);
            
            document.add(new Paragraph("\n"));

            // Tabela de palestrantes
            Table table = new Table(4);
            table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Nome").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Email").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Especialidade").setBold()));

            for (PalestranteDTO palestrante : palestrantes) {
                table.addCell(new Cell().add(new Paragraph(palestrante.getId().toString())));
                table.addCell(new Cell().add(new Paragraph(palestrante.getNome())));
                table.addCell(new Cell().add(new Paragraph(palestrante.getEmail())));
                table.addCell(new Cell().add(new Paragraph(palestrante.getEspecialidade() != null ? palestrante.getEspecialidade() : "N/A")));
            }

            document.add(table);
            
            // Resumo
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de palestrantes: " + palestrantes.size()).setBold());
            
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }
}