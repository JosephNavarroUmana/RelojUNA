package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.servicio.ConsultaService;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.EJB;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.time.LocalDate;

@WebService(serviceName = "ConsultaWS")
public class ConsultaWS {

    @EJB
    private ConsultaService consultaService;

    @WebMethod(operationName = "consultarMarcas")
    public Respuesta consultarMarcas(
            @WebParam(name = "desde") String desdeTexto,
            @WebParam(name = "hasta") String hastaTexto,
            @WebParam(name = "empleadoId") Long empleadoId) {

        LocalDate desde = LocalDate.parse(desdeTexto);
        LocalDate hasta = LocalDate.parse(hastaTexto);

        return consultaService.consultarMarcas(desde, hasta, empleadoId);
    }
}