package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.dto.ListaEmpleadoDto;
import cr.ac.una.relojservidor.servicio.EmpleadoService;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.EJB;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;

//Le avisamos a JAXB que estas clases pueden viajar escondidas dentro de Respuesta.resultado
@XmlSeeAlso({EmpleadoDto.class, ListaEmpleadoDto.class})
@WebService(serviceName = "EmpleadoWS")
public class EmpleadoWS {

    @EJB
    private EmpleadoService empleadoService;

    @WebMethod(operationName = "guardarEmpleado")
    public Respuesta guardarEmpleado(@WebParam(name = "empleado") EmpleadoDto empleado) {
        return empleadoService.guardar(empleado);
    }

    @WebMethod(operationName = "obtenerEmpleados")
    public Respuesta obtenerEmpleados() {
        return empleadoService.obtenerTodos();
    }

    @WebMethod(operationName = "obtenerEmpleadoPorId")
    public Respuesta obtenerEmpleadoPorId(@WebParam(name = "id") Long id) {
        return empleadoService.obtenerPorId(id);
    }

    @WebMethod(operationName = "eliminarEmpleado")
    public Respuesta eliminarEmpleado(@WebParam(name = "id") Long id) {
        return empleadoService.eliminar(id);
    }
}