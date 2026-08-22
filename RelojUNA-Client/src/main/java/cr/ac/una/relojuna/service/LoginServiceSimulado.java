package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import java.util.List;

public class LoginServiceSimulado implements ILoginService {

    @Override
    public EmpleadoDto validarLogin(String folio, String clave) {
        //Reutilizamos la lista de empleados simulados para validar
        IEmpleadoService empleadoService = new EmpleadoServiceSimulado();
        List<EmpleadoDto> empleados = empleadoService.buscarEmpleados("");

        for (EmpleadoDto empleado : empleados) {
            String folioEmpleado = empleado.getFolio().toString();

            if (folioEmpleado.equals(folio) && empleado.getClave().equals(clave)) {
                return empleado;
            }
        }

        //Si no encontro coincidencia, retornamos null
        return null;
    }
}