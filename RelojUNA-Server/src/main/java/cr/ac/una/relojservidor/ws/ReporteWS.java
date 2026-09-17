package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.dto.ArchivoDto;
import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.dto.ListaConsultaFilaDto;
import cr.ac.una.relojservidor.dto.ListaEmpleadoDto;
import cr.ac.una.relojservidor.dto.ListaPlanillaFilaDto;
import cr.ac.una.relojservidor.servicio.ExcelService;
import cr.ac.una.relojservidor.servicio.JasperService;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({ArchivoDto.class, ListaConsultaFilaDto.class, ListaPlanillaFilaDto.class, ListaEmpleadoDto.class, EmpleadoDto.class})
@WebService(serviceName = "ReporteWS")
public class ReporteWS {

    @EJB
    private ExcelService excelService;

    @EJB
    private JasperService jasperService;

    @WebMethod(operationName = "exportarConsultaExcel")
    public Respuesta exportarConsultaExcel(@WebParam(name = "filas") ListaConsultaFilaDto filas) {
        return excelService.exportarConsultaFilasExcel(filas.getFilas());
    }

    @WebMethod(operationName = "exportarPlanillaExcel")
    public Respuesta exportarPlanillaExcel(@WebParam(name = "filas") ListaPlanillaFilaDto filas) {
        return excelService.exportarPlanillaExcel(filas.getFilas());
    }

    @WebMethod(operationName = "generarReporteEmpleadosPdf")
    public Respuesta generarReporteEmpleadosPdf(@WebParam(name = "empleados") ListaEmpleadoDto empleados) {
        return jasperService.generarReporteEmpleadosPdf(empleados.getEmpleados());
    }

    @WebMethod(operationName = "generarReporteMarcasPdf")
    public Respuesta generarReporteMarcasPdf(@WebParam(name = "filas") ListaConsultaFilaDto filas) {
        return jasperService.generarReporteMarcasPdf(filas.getFilas());
    }
}