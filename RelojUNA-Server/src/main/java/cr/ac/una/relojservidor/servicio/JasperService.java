package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.ArchivoDto;
import cr.ac.una.relojservidor.dto.ConsultaFilaDto;
import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.Stateless;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import jakarta.ejb.EJB;

@Stateless
public class JasperService {    
@EJB
private EmpleadoService empleadoService;
    
  public Respuesta generarReporteEmpleadosPdf(List<EmpleadoDto> empleados) {
    try {
        //leen los empleados para que el folio venga completo
        List<EmpleadoDto> empleadosBD = empleadoService.obtenerTodosLosDtos();
        JasperPrint reporteLleno = llenarReporteEmpleados(empleadosBD);
        byte[] bytesPdf = exportarAPdf(reporteLleno);
        return new Respuesta(true, "Reporte generado con exito", new ArchivoDto(bytesPdf));
    } catch (Exception ex) {
        return new Respuesta(false, "Error al generar el reporte de empleados: " + ex.getMessage());
    }
}

    public Respuesta generarReporteMarcasPdf(List<ConsultaFilaDto> filas) {
        try {
            JasperPrint reporteLleno = llenarReporteMarcas(filas);
            byte[] bytesPdf = exportarAPdf(reporteLleno);
            return new Respuesta(true, "Reporte generado con exito", new ArchivoDto(bytesPdf));
        } catch (Exception ex) {
            return new Respuesta(false, "Error al generar el reporte de marcas: " + ex.getMessage());
        }
    }

  //Carga el jasper ya compilado de empleados y lo llena con la lista
private JasperPrint llenarReporteEmpleados(List<EmpleadoDto> empleados) throws Exception {
    String rutaReporte = "/cr/ac/una/relojservidor/reportes/ReporteEmpleados.jasper";
    InputStream flujoReporte = JasperService.class.getResourceAsStream(rutaReporte);

    if (flujoReporte == null) {
        throw new Exception("No se encontro el archivo " + rutaReporte + " en el classpath del servidor");
    }

    JRBeanCollectionDataSource fuenteDatos = new JRBeanCollectionDataSource(empleados);
    Map<String, Object> parametros = new HashMap<>();

    return JasperFillManager.fillReport(flujoReporte, parametros, fuenteDatos);
}

private JasperPrint llenarReporteMarcas(List<ConsultaFilaDto> filas) throws Exception {
    String rutaReporte = "/cr/ac/una/relojservidor/reportes/ReporteMarcas.jasper";
    InputStream flujoReporte = JasperService.class.getResourceAsStream(rutaReporte);

    if (flujoReporte == null) {
        throw new Exception("No se encontro el archivo " + rutaReporte + " en el classpath del servidor");
    }

    JRBeanCollectionDataSource fuenteDatos = new JRBeanCollectionDataSource(filas);
    Map<String, Object> parametros = new HashMap<>();

    return JasperFillManager.fillReport(flujoReporte, parametros, fuenteDatos);
}



    private byte[] exportarAPdf(JasperPrint reporteLleno) throws Exception {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        JRPdfExporter exportadorPdf = new JRPdfExporter();
        exportadorPdf.setExporterInput(new SimpleExporterInput(reporteLleno));
        exportadorPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(salida));
        exportadorPdf.exportReport();

        return salida.toByteArray();
    }
}