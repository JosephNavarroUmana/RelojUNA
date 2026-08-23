package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.servicio.EmpleadoService;
import cr.ac.una.relojservidor.util.Respuesta;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "EmpleadoWS")
public class EmpleadoWS {

    private final EmpleadoService empleadoService = new EmpleadoService();

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