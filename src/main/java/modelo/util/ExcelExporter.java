/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.util;

/**
 *
 * @author drola
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class ExcelExporter {

    // Método estático: Dinámico para cualquier JTable
    public static void exportar(JTable tabla, String tituloHoja) {
        // 1. Pedir al usuario dónde guardar el archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte Excel");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel (*.xlsx)", "xlsx"));
        
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            // Asegurar que termine en .xlsx
            if (!archivo.getName().endsWith(".xlsx")) {
                archivo = new File(archivo.getAbsolutePath() + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet(tituloHoja);
                TableModel modelo = tabla.getModel();

                // 2. Crear Estilo para la Cabecera (Fondo oscuro, texto blanco, negrita)
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                
                Font headerFont = workbook.createFont();
                headerFont.setColor(IndexedColors.WHITE.getIndex());
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                // Estilo general con bordes
                CellStyle bordeStyle = workbook.createCellStyle();
                bordeStyle.setBorderTop(BorderStyle.THIN);
                bordeStyle.setBorderBottom(BorderStyle.THIN);
                bordeStyle.setBorderLeft(BorderStyle.THIN);
                bordeStyle.setBorderRight(BorderStyle.THIN);

                // 3. Imprimir Cabeceras Dinámicas
                Row headerRow = sheet.createRow(0);
                for (int col = 0; col < modelo.getColumnCount(); col++) {
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(modelo.getColumnName(col)); // Toma el nombre automáticamente
                    cell.setCellStyle(headerStyle);
                }

                // 4. Imprimir Datos Dinámicamente
                for (int row = 0; row < modelo.getRowCount(); row++) {
                    Row excelRow = sheet.createRow(row + 1);
                    for (int col = 0; col < modelo.getColumnCount(); col++) {
                        Cell cell = excelRow.createCell(col);
                        Object valor = modelo.getValueAt(row, col);
                        
                        cell.setCellStyle(bordeStyle); // Aplicar bordes

                        // Clasificar el tipo de dato para que Excel lo entienda
                        if (valor != null) {
                            if (valor instanceof Number) {
                                cell.setCellValue(((Number) valor).doubleValue());
                            } else if (valor instanceof LocalDate) {
                                cell.setCellValue(valor.toString()); // Opcional: Crear CellStyle de fecha
                            } else {
                                cell.setCellValue(valor.toString());
                            }
                        }
                    }
                }

                // 5. Auto-ajustar el ancho de las columnas (Toque profesional)
                for (int col = 0; col < modelo.getColumnCount(); col++) {
                    sheet.autoSizeColumn(col);
                }

                // 6. Escribir el archivo
                try (FileOutputStream out = new FileOutputStream(archivo)) {
                    workbook.write(out);
                }

                JOptionPane.showMessageDialog(null, "¡Exportación exitosa!\nGuardado en: " + archivo.getName(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
