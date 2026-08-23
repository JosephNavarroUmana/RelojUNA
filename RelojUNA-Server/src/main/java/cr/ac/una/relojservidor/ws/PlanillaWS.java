package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.dto.PlanillaDto;
import cr.ac.una.relojservidor.servicio.PlanillaService;
import cr.ac.una.relojservidor.util.Respuesta;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "PlanillaWS")
public class PlanillaWS {

    private final PlanillaService planillaService = new PlanillaService();

    @WebMethod(operationName = "guardarPlanilla")
    public Respuesta guardarPlanilla(@WebParam(name = "planilla") PlanillaDto planilla) {
        return planillaService.guardar(planilla);
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