package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.ConsultaResultadoDto;
import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.util.Respuesta;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelService {

    /**
     * Genera un Excel con formato a partir del resultado de una consulta
     * de marcas (punto 8), devolviendo el archivo como arreglo de bytes
     * para que el cliente lo reciba por SOAP y lo guarde en disco.
     */
    public Respuesta exportarConsultaExcel(ConsultaResultadoDto consulta) {
        try (XSSFWorkbook libro = new XSSFWorkbook()) {
            XSSFSheet hoja = libro.createSheet("Consulta de Marcas");

            CellStyle estiloEncabezado = crearEstiloEncabezado(libro);

            // --- Fila de resumen (totales) ---
            Row filaResumen = hoja.createRow(0);
            filaResumen.createCell(0).setCellValue("Cantidad de empleados:");
            filaResumen.createCell(1).setCellValue(consulta.getCantidadEmpleados());

            Row filaResumen2 = hoja.createRow(1);
            filaResumen2.createCell(0).setCellValue("Total de marcas:");
            filaResumen2.createCell(1).setCellValue(consulta.getTotalMarcas());

            Row filaResumen3 = hoja.createRow(2);
            filaResumen3.createCell(0).setCellValue("Total de horas trabajadas:");
            filaResumen3.createCell(1).setCellValue(consulta.getTotalHorasTrabajadas());

            // --- Encabezados de la tabla de detalle ---
            String[] encabezados = {"ID Empleado", "Fecha", "Hora", "Tipo"};
            Row filaEncabezado = hoja.createRow(4);
            for (int i = 0; i < encabezados.length; i++) {
                Cell celda = filaEncabezado.createCell(i);
                celda.setCellValue(encabezados[i]);
                celda.setCellStyle(estiloEncabezado);
            }

            // --- Detalle de marcas ---
            int numeroFila = 5;
            for (MarcaDto marca : consulta.getMarcas()) {
                Row fila = hoja.createRow(numeroFila);
                fila.createCell(0).setCellValue(marca.getEmpleadoId());
                fila.createCell(1).setCellValue(marca.getFecha().toString());
                fila.createCell(2).setCellValue(marca.getHora().toString());
                fila.createCell(3).setCellValue(marca.getTipo());
                numeroFila++;
            }

            for (int i = 0; i < encabezados.length; i++) {
                hoja.autoSizeColumn(i);
            }

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            libro.write(salida);

            return new Respuesta(true, "Excel generado con éxito", salida.toByteArray());

        } catch (IOException e) {
            return new Respuesta(false, "Error al generar el Excel: " + e.getMessage());
        }
    }

    private CellStyle crearEstiloEncabezado(XSSFWorkbook libro) {
        Font fuenteNegrita = libro.createFont();
        fuenteNegrita.setBold(true);

        CellStyle estilo = libro.createCellStyle();
        estilo.setFont(fuenteNegrita);
        return estilo;
    }
}