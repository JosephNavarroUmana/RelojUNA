package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.dto.PlanillaDto;
import cr.ac.una.relojservidor.servicio.PlanillaService;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.EJB;
import cr.ac.una.relojservidor.dto.ListaPlanillaFilaDto;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@XmlSeeAlso({ListaPlanillaFilaDto.class})
@WebService(serviceName = "PlanillaWS")
public class PlanillaWS {

    @EJB
    private PlanillaService planillaService;

    @WebMethod(operationName = "guardarPlanilla")
    public Respuesta guardarPlanilla(@WebParam(name = "planilla") PlanillaDto planilla) {
        return planillaService.guardar(planilla);
    }
    
    @WebMethod(operationName = "generarPlanilla")
    public Respuesta generarPlanilla(@WebParam(name = "mes") int mes, @WebParam(name = "anio") int anio) {
        return planillaService.generarPlanillaFilas(mes, anio);
    }

    @WebMethod(operationName = "obtenerPlanillas")
    public Respuesta obtenerPlanillas() {
        return planillaService.obtenerTodos();
    }

    @WebMethod(operationName = "obtenerPlanillaPorId")
    public Respuesta obtenerPlanillaPorId(@WebParam(name = "id") Long id) {
        return planillaService.obtenerPorId(id);
    }
}