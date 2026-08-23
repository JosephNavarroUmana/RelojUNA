package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.servicio.ConsultaService;
import cr.ac.una.relojservidor.util.Respuesta;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.time.LocalDate;

@WebService(serviceName = "ConsultaWS")
public class ConsultaWS {

    private final ConsultaService consultaService = new ConsultaService();

    @WebMethod(operationName = "consultarMarcas")
    public Respuesta consultarMarcas(
            @WebParam(name = "desde") LocalDate desde,
            @WebParam(name = "hasta") LocalDate hasta,
            @WebParam(name = "empleadoId") Long empleadoId) {
        return consultaService.consultarMarcas(desde, hasta, empleadoId);
    }
}