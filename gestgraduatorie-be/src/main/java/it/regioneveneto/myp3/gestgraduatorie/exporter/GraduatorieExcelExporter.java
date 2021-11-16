package it.regioneveneto.myp3.gestgraduatorie.exporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import it.regioneveneto.myp3.gestgraduatorie.web.dto.Bean;

/**
 * <code>GraduatorieExcelExporter</code> 
 * permette l'esportazione 
 * della lista delle graduatorie in un file XLS
 * 
 * @since  25th march 2021
 * @author dario granato
 *
 */
public class GraduatorieExcelExporter {
	
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Collection<Bean> richiesteList;
     
    public GraduatorieExcelExporter( Collection<Bean> richiesteList) {
        this.richiesteList = richiesteList;
        workbook = new XSSFWorkbook();
    }
 
 //metodo incaricato della scrittura dell'header
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Graduatoria");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);
         
        createCell(row, 0, "Id Bando", style);      
        createCell(row, 1, "Id Richiesta", style);       
        createCell(row, 2, "Bando", style);    
        createCell(row, 3, "Num. Protocollo", style);
        createCell(row, 4, "Nome", style);
        createCell(row, 5, "Cognome", style);
        createCell(row, 6, "Codice Fiscale", style);
        createCell(row, 7, "Punteggio", style);
         
    }
    //metodo incaricato della creazione delle celle
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(11);
        style.setFont(font);
                 
        for (Bean richiesta : richiesteList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, richiesta.getIdBando(), style);
            createCell(row, columnCount++, richiesta.getIdRichiesta(), style);
            createCell(row, columnCount++, richiesta.getBando(), style);
            createCell(row, columnCount++, richiesta.getNum_protocollo(), style);
            createCell(row, columnCount++, richiesta.getNome(), style);
            createCell(row, columnCount++, richiesta.getCognome(), style);
            createCell(row, columnCount++, richiesta.getCodice_fiscale(), style);
            createCell(row, columnCount++, richiesta.getPunteggio(), style);
             
        }
    }
     
    public byte[]  export() throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] xls = baos.toByteArray(); 
        
        workbook.close();
        
        return xls;
         
    }

}
