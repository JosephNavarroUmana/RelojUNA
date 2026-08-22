package cr.ac.una.relojuna.util;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.PlanillaDto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExportador {

    // Exporta el resultado de la consulta de marcas a un archivo de Excel
    public static void exportarConsultas(List<ConsultaResultadoDto> datos, File archivo) throws IOException {
        XSSFWorkbook libro = new XSSFWorkbook();
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
        for (ConsultaResultadoDto fila : datos) {
            Row filaExcel = hoja.createRow(numeroFila);
            filaExcel.createCell(0).setCellValue(fila.getNombreEmpleado());
            filaExcel.createCell(1).setCellValue(fila.getFecha().toString());
            filaExcel.createCell(2).setCellValue(fila.getHoraEntrada().toString());
            filaExcel.createCell(3).setCellValue(fila.getHoraSalida().toString());
            filaExcel.createCell(4).setCellValue(fila.getHorasTrabajadas());
            numeroFila = numeroFila + 1;
        }

        for (int i = 0; i < encabezados.length; i++) {
            hoja.autoSizeColumn(i);
        }

        guardarLibro(libro, archivo);
    }

    // Exporta la planilla generada a un archivo de Excel
    public static void exportarPlanilla(List<PlanillaDto> datos, File archivo) throws IOException {
        XSSFWorkbook libro = new XSSFWorkbook();
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
        for (PlanillaDto fila : datos) {
            Row filaExcel = hoja.createRow(numeroFila);
            filaExcel.createCell(0).setCellValue(fila.getNombreEmpleado());
            filaExcel.createCell(1).setCellValue(fila.getHorasOrdinarias());
            filaExcel.createCell(2).setCellValue(fila.getHorasExtras());
            filaExcel.createCell(3).setCellValue(fila.getHorasDobles());
            filaExcel.createCell(4).setCellValue(fila.getSalarioMensual());
            numeroFila = numeroFila + 1;
        }

        for (int i = 0; i < encabezados.length; i++) {
            hoja.autoSizeColumn(i);
        }

        guardarLibro(libro, archivo);
    }

    // Crea un estilo de letra en negrita, usado en la fila de encabezados
    private static CellStyle crearEstiloEncabezado(XSSFWorkbook libro) {
        Font fuenteNegrita = libro.createFont();
        fuenteNegrita.setBold(true);

        CellStyle estilo = libro.createCellStyle();
        estilo.setFont(fuenteNegrita);
        return estilo;
    }

    // Escribe el libro de excel en el archivo indicado
    private static void guardarLibro(XSSFWorkbook libro, File archivo) throws IOException {
        FileOutputStream salida = new FileOutputStream(archivo);
        libro.write(salida);
        salida.close();
        libro.close();
    }
}