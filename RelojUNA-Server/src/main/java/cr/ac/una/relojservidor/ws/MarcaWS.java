package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.dto.ListaMarcaDto;
import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.servicio.MarcaService;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.EJB;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({MarcaDto.class, ListaMarcaDto.class})
@WebService(serviceName = "MarcaWS")
public class MarcaWS {

    @EJB
    private MarcaService marcaService;

    @WebMethod(operationName = "marcar")
    public Respuesta marcar(@WebParam(name = "empleadoId") Long empleadoId) {
        return marcaService.marcar(empleadoId);
    }

    @WebMethod(operationName = "guardarMarca")
    public Respuesta guardarMarca(@WebParam(name = "marca") MarcaDto marca) {
        return marcaService.guardar(marca);
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