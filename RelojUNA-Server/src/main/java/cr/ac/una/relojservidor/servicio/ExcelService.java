package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.ArchivoDto;
import cr.ac.una.relojservidor.dto.ConsultaFilaDto;
import cr.ac.una.relojservidor.dto.ConsultaResultadoDto;
import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.dto.PlanillaFilaDto;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.Stateless;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Stateless
public class ExcelService {

    public Respuesta exportarConsultaExcel(ConsultaResultadoDto consulta) {
        try (XSSFWorkbook libro = new XSSFWorkbook()) {
            XSSFSheet hoja = libro.createSheet("Consulta de Marcas");

            CellStyle estiloEncabezado = crearEstiloEncabezado(libro);

            Row filaResumen = hoja.createRow(0);
            filaResumen.createCell(0).setCellValue("Cantidad de empleados:");
            filaResumen.createCell(1).setCellValue(consulta.getCantidadEmpleados());

            Row filaResumen2 = hoja.createRow(1);
            filaResumen2.createCell(0).setCellValue("Total de marcas:");
            filaResumen2.createCell(1).setCellValue(consulta.getTotalMarcas());

            Row filaResumen3 = hoja.createRow(2);
            filaResumen3.createCell(0).setCellValue("Total de horas trabajadas:");
            filaResumen3.createCell(1).setCellValue(consulta.getTotalHorasTrabajadas());

            String[] encabezados = {"ID Empleado", "Fecha", "Hora", "Tipo"};
            Row filaEncabezado = hoja.createRow(4);
            for (int i = 0; i < encabezados.length; i++) {
                Cell celda = filaEncabezado.createCell(i);
                celda.setCellValue(encabezados[i]);
                celda.setCellStyle(estiloEncabezado);
            }

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

            return new Respuesta(true, "Excel generado con exito", new ArchivoDto(salida.toByteArray()));

        } catch (IOException e) {
            return new Respuesta(false, "Error al generar el Excel: " + e.getMessage());
        }
    }

    //Genera el Excel de la pantalla de consultas con streams, una fila por empleado y dia
    public Respuesta exportarConsultaFilasExcel(List<ConsultaFilaDto> filas) {
        try (XSSFWorkbook libro = new XSSFWorkbook()) {
            XSSFSheet hoja = libro.createSheet("Consulta de Marcas");

            CellStyle estiloEncabezado = crearEstiloEncabezado(libro);

            String[] encabezados = {"Empleado", "Fecha", "Hora Entrada", "Hora Salida", "Horas Trabajadas"};
            Row filaEncabezado = hoja.createRow(0);
            for (int i = 0; i < encabezados.length; i++) {
                Cell celda = filaEncabezado.createCell(i);
                celda.setCellValue(encabezados[i]);
                celda.setCellStyle(estiloEncabezado);
            }

            int numeroFila = 1;
            for (ConsultaFilaDto fila : filas) {
                Row filaExcel = hoja.createRow(numeroFila);
                filaExcel.createCell(0).setCellValue(fila.getNombreEmpleado());
                filaExcel.createCell(1).setCellValue(fila.getFecha().toString());
                filaExcel.createCell(2).setCellValue(fila.getHoraEntrada().toString());
                filaExcel.createCell(3).setCellValue(fila.getHoraSalida().toString());
                filaExcel.createCell(4).setCellValue(fila.getHorasTrabajadas());
                numeroFila++;
            }

            for (int i = 0; i < encabezados.length; i++) {
                hoja.autoSizeColumn(i);
            }

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            libro.write(salida);

            return new Respuesta(true, "Excel generado con exito", new ArchivoDto(salida.toByteArray()));

        } catch (IOException e) {
            return new Respuesta(false, "Error al generar el Excel: " + e.getMessage());
        }
    }

    //Genera el Excel de la planilla, una fila por empleado con sus horas y salario
    public Respuesta exportarPlanillaExcel(List<PlanillaFilaDto> filas) {
        try (XSSFWorkbook libro = new XSSFWorkbook()) {
            XSSFSheet hoja = libro.createSheet("Planilla");

            CellStyle estiloEncabezado = crearEstiloEncabezado(libro);

            String[] encabezados = {"Empleado", "Horas Ordinarias", "Horas Extras", "Horas Dobles", "Salario Mensual"};
            Row filaEncabezado = hoja.createRow(0);
            for (int i = 0; i < encabezados.length; i++) {
                Cell celda = filaEncabezado.createCell(i);
                celda.setCellValue(encabezados[i]);
                celda.setCellStyle(estiloEncabezado);
            }

            int numeroFila = 1;
            for (PlanillaFilaDto fila : filas) {
                Row filaExcel = hoja.createRow(numeroFila);
                filaExcel.createCell(0).setCellValue(fila.getNombreEmpleado());
                filaExcel.createCell(1).setCellValue(fila.getHorasOrdinarias());
                filaExcel.createCell(2).setCellValue(fila.getHorasExtras());
                filaExcel.createCell(3).setCellValue(fila.getHorasDobles());
                filaExcel.createCell(4).setCellValue(fila.getSalarioMensual());
                numeroFila++;
            }

            for (int i = 0; i < encabezados.length; i++) {
                hoja.autoSizeColumn(i);
            }

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            libro.write(salida);

            return new Respuesta(true, "Excel generado con exito", new ArchivoDto(salida.toByteArray()));

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