package cr.ac.una.relojservidor;

import cr.ac.una.relojservidor.dto.ConsultaResultadoDto;
import cr.ac.una.relojservidor.servicio.ConsultaService;
import cr.ac.una.relojservidor.servicio.ExcelService;
import cr.ac.una.relojservidor.servicio.MarcaService;
import cr.ac.una.relojservidor.util.Respuesta;
import java.time.LocalDate;

public class TestConexion {
    public static void main(String[] args) {
        MarcaService marcaService = new MarcaService();

        // --- Consultar marcas de agosto 2026 (todos los empleados) ---
        ConsultaService consultaService = new ConsultaService();
        Respuesta r = consultaService.consultarMarcas(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31), null);
        System.out.println(r.getMensaje());

        ConsultaResultadoDto resultado = (ConsultaResultadoDto) r.getResultado();
        System.out.println("Cantidad empleados: " + resultado.getCantidadEmpleados());
        System.out.println("Total marcas: " + resultado.getTotalMarcas());
        System.out.println("Total horas trabajadas: " + resultado.getTotalHorasTrabajadas());
        System.out.println("Cantidad de marcas en la lista: " + resultado.getMarcas().size());

        // --- Generar el Excel a partir de esa consulta ---
        ExcelService excelService = new ExcelService();
        Respuesta rExcel = excelService.exportarConsultaExcel(resultado);
        byte[] excelBytes = (byte[]) rExcel.getResultado();

        try {
            java.nio.file.Files.write(java.nio.file.Path.of("prueba.xlsx"), excelBytes);
            System.out.println("Excel guardado, revisa prueba.xlsx");
        } catch (java.io.IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
}