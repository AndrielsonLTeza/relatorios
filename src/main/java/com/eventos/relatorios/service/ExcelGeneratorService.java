// ExcelGeneratorService.java
package com.eventos.relatorios.service;

import com.eventos.relatorios.dto.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ExcelGeneratorService {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] gerarRelatorioEventos(List<EventoDTO> eventos) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Eventos");

        // Estilo do cabeçalho
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Cabeçalho
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nome", "Descrição", "Data Início", "Data Fim", "Local", "Capacidade", "Status", "Categoria"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dados
        int rowNum = 1;
        for (EventoDTO evento : eventos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(evento.getId());
            row.createCell(1).setCellValue(evento.getNome());
            row.createCell(2).setCellValue(evento.getDescricao() != null ? evento.getDescricao() : "");
            row.createCell(3).setCellValue(evento.getDataInicio() != null ? evento.getDataInicio().format(dateFormatter) : "");
            row.createCell(4).setCellValue(evento.getDataFim() != null ? evento.getDataFim().format(dateFormatter) : "");
            row.createCell(5).setCellValue(evento.getLocal() != null ? evento.getLocal() : "");
            row.createCell(6).setCellValue(evento.getCapacidadeMaxima() != null ? evento.getCapacidadeMaxima() : 0);
            row.createCell(7).setCellValue(evento.getStatus() != null ? evento.getStatus() : "");
            row.createCell(8).setCellValue(evento.getCategoria() != null ? evento.getCategoria() : "");
        }

        // Auto-size das colunas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    public byte[] gerarRelatorioInscricoes(List<InscricaoDTO> inscricoes) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inscrições");

        // Estilo do cabeçalho
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Cabeçalho
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Usuário ID", "Evento ID", "Nome do Evento", "Data Inscrição", "Status"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dados
        int rowNum = 1;
        for (InscricaoDTO inscricao : inscricoes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(inscricao.getId());
            row.createCell(1).setCellValue(inscricao.getUsuarioId());
            row.createCell(2).setCellValue(inscricao.getEventoId());
            row.createCell(3).setCellValue(inscricao.getEvento() != null ? inscricao.getEvento().getNome() : "");
            row.createCell(4).setCellValue(inscricao.getDataInscricao() != null ? inscricao.getDataInscricao().format(dateFormatter) : "");
            row.createCell(5).setCellValue(inscricao.getStatus() != null ? inscricao.getStatus() : "");
        }

        // Auto-size das colunas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    public byte[] gerarRelatorioPalestrantes(List<PalestranteDTO> palestrantes) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Palestrantes");

        // Estilo do cabeçalho
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Cabeçalho
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nome", "Email", "Especialidade", "Instituição", "Biografia"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dados
        int rowNum = 1;
        for (PalestranteDTO palestrante : palestrantes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(palestrante.getId());
            row.createCell(1).setCellValue(palestrante.getNome());
            row.createCell(2).setCellValue(palestrante.getEmail());
            row.createCell(3).setCellValue(palestrante.getEspecialidade() != null ? palestrante.getEspecialidade() : "");
            row.createCell(4).setCellValue(palestrante.getInstituicao() != null ? palestrante.getInstituicao() : "");
            row.createCell(5).setCellValue(palestrante.getBiografia() != null ? palestrante.getBiografia() : "");
        }

        // Auto-size das colunas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    public byte[] gerarRelatorioConsolidado(List<EventoDTO> eventos, List<InscricaoDTO> inscricoes, 
                                          List<PalestranteDTO> palestrantes) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        // Estilo do cabeçalho
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Sheet de Resumo
        Sheet resumoSheet = workbook.createSheet("Resumo");
        Row resumoHeader = resumoSheet.createRow(0);
        resumoHeader.createCell(0).setCellValue("Tipo");
        resumoHeader.createCell(1).setCellValue("Quantidade");
        resumoHeader.getCell(0).setCellStyle(headerStyle);
        resumoHeader.getCell(1).setCellStyle(headerStyle);

        resumoSheet.createRow(1).createCell(0).setCellValue("Total de Eventos");
        resumoSheet.getRow(1).createCell(1).setCellValue(eventos.size());
        resumoSheet.createRow(2).createCell(0).setCellValue("Total de Inscrições");
        resumoSheet.getRow(2).createCell(1).setCellValue(inscricoes.size());
        resumoSheet.createRow(3).createCell(0).setCellValue("Total de Palestrantes");
        resumoSheet.getRow(3).createCell(1).setCellValue(palestrantes.size());

        resumoSheet.autoSizeColumn(0);
        resumoSheet.autoSizeColumn(1);

        // Adicionar outros sheets
        adicionarSheetEventos(workbook, eventos, headerStyle);
        adicionarSheetInscricoes(workbook, inscricoes, headerStyle);
        adicionarSheetPalestrantes(workbook, palestrantes, headerStyle);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    private void adicionarSheetEventos(Workbook workbook, List<EventoDTO> eventos, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Eventos");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nome", "Data Início", "Local", "Status"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (EventoDTO evento : eventos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(evento.getId());
            row.createCell(1).setCellValue(evento.getNome());
            row.createCell(2).setCellValue(evento.getDataInicio() != null ? evento.getDataInicio().format(dateFormatter) : "");
            row.createCell(3).setCellValue(evento.getLocal() != null ? evento.getLocal() : "");
            row.createCell(4).setCellValue(evento.getStatus() != null ? evento.getStatus() : "");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void adicionarSheetInscricoes(Workbook workbook, List<InscricaoDTO> inscricoes, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Inscrições");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Usuário", "Evento", "Data Inscrição"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (InscricaoDTO inscricao : inscricoes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(inscricao.getId());
            row.createCell(1).setCellValue(inscricao.getUsuarioId());
            row.createCell(2).setCellValue(inscricao.getEvento() != null ? inscricao.getEvento().getNome() : "");
            row.createCell(3).setCellValue(inscricao.getDataInscricao() != null ? inscricao.getDataInscricao().format(dateFormatter) : "");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void adicionarSheetPalestrantes(Workbook workbook, List<PalestranteDTO> palestrantes, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Palestrantes");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nome", "Email", "Especialidade"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (PalestranteDTO palestrante : palestrantes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(palestrante.getId());
            row.createCell(1).setCellValue(palestrante.getNome());
            row.createCell(2).setCellValue(palestrante.getEmail());
            row.createCell(3).setCellValue(palestrante.getEspecialidade() != null ? palestrante.getEspecialidade() : "");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
