package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import java.util.List;

public interface IEmpleadoService {

    //Busca empleados por nombre o folio, si el texto viene vacio trae todos
    List<EmpleadoDto> buscarEmpleados(String texto);

    //Guarda un empleado nuevo o actualiza uno existente segun el folio
    EmpleadoDto guardarEmpleado(EmpleadoDto empleado);

    //Elimina un empleado segun su folio
    void eliminarEmpleado(Integer folio);
}