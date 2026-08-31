package cr.ac.una.relojuna.util;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.EmpleadoDto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.swing.JRViewer;

public class JasperExportador {

    // Muestra en una ventana aparte la vista previa del reporte de empleados
    public static void mostrarVistaPreviaEmpleados(List<EmpleadoDto> empleados) throws Exception {
        JasperPrint reporteLleno = llenarReporteEmpleados(empleados);

        JRViewer visor = new JRViewer(reporteLleno);
        JFrame ventana = new JFrame("Vista Previa - Reporte de Empleados");
        ventana.add(visor);
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setVisible(true);
    }

    // Genera el reporte de empleados y lo guarda como PDF en el archivo indicado
    public static void exportarEmpleadosAPdf(List<EmpleadoDto> empleados, File archivo) throws Exception {
        JasperPrint reporteLleno = llenarReporteEmpleados(empleados);

        JRPdfExporter exportadorPdf = new JRPdfExporter();
        exportadorPdf.setExporterInput(new SimpleExporterInput(reporteLleno));
        exportadorPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(archivo)));
        exportadorPdf.exportReport();
    }

    // Compila el jrxml del disco y lo llena con la lista de empleados
    private static JasperPrint llenarReporteEmpleados(List<EmpleadoDto> empleados) throws Exception {
        String rutaReporte = "/cr/ac/una/relojuna/reportes/ReporteEmpleados.jrxml";
        InputStream flujoReporte = JasperExportador.class.getResourceAsStream(rutaReporte);

        JasperReport reporteCompilado = JasperCompileManager.compileReport(flujoReporte);

        JRBeanCollectionDataSource fuenteDatos = new JRBeanCollectionDataSource(empleados);
        Map<String, Object> parametros = new HashMap<>();

        JasperPrint reporteLleno = JasperFillManager.fillReport(reporteCompilado, parametros, fuenteDatos);
        return reporteLleno;
    }
    // Muestra en una ventana aparte la vista previa del reporte de marcas
public static void mostrarVistaPreviaMarcas(List<ConsultaResultadoDto> marcas) throws Exception {
    JasperPrint reporteLleno = llenarReporteMarcas(marcas);

    JRViewer visor = new JRViewer(reporteLleno);
    JFrame ventana = new JFrame("Vista Previa - Reporte de Marcas");
    ventana.add(visor);
    ventana.setSize(800, 600);
    ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventana.setVisible(true);
}

// Genera el reporte de marcas y lo guarda como PDF en el archivo indicado
public static void exportarMarcasAPdf(List<ConsultaResultadoDto> marcas, File archivo) throws Exception {
    JasperPrint reporteLleno = llenarReporteMarcas(marcas);

    JRPdfExporter exportadorPdf = new JRPdfExporter();
    exportadorPdf.setExporterInput(new SimpleExporterInput(reporteLleno));
    exportadorPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(archivo)));
    exportadorPdf.exportReport();
}

// Compila el jrxml del disco y lo llena con la lista de marcas
private static JasperPrint llenarReporteMarcas(List<ConsultaResultadoDto> marcas) throws Exception {
    String rutaReporte = "/cr/ac/una/relojuna/reportes/ReporteMarcas.jrxml";
    InputStream flujoReporte = JasperExportador.class.getResourceAsStream(rutaReporte);

    JasperReport reporteCompilado = JasperCompileManager.compileReport(flujoReporte);

    JRBeanCollectionDataSource fuenteDatos = new JRBeanCollectionDataSource(marcas);
    Map<String, Object> parametros = new HashMap<>();

    JasperPrint reporteLleno = JasperFillManager.fillReport(reporteCompilado, parametros, fuenteDatos);
    return reporteLleno;
}
}