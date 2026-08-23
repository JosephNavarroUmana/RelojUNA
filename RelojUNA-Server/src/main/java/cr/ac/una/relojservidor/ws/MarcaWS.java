package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.servicio.MarcaService;
import cr.ac.una.relojservidor.util.Respuesta;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "MarcaWS")
public class MarcaWS {

    private final MarcaService marcaService = new MarcaService();

    @WebMethod(operationName = "guardarMarca")
    public Respuesta guardarMarca(@WebParam(name = "marca") MarcaDto marca) {
        return marcaService.guardar(marca);
    }

    @WebMethod(operationName = "marcar")
    public Respuesta marcar(@WebParam(name = "empleadoId") Long empleadoId) {
        return marcaService.marcar(empleadoId);
    }

    @WebMethod(operationName = "obtenerMarcas")
    public Respuesta obtenerMarcas() {
        return marcaService.obtenerTodas();
    }

    @WebMethod(operationName = "eliminarMarca")
    public Respuesta eliminarMarca(@WebParam(name = "id") Long id) {
        return marcaService.eliminar(id);
    }

    @WebMethod(operationName = "buscarInconsistencias")
    public Respuesta buscarInconsistencias() {
        return marcaService.buscarInconsistencias();
    }
}