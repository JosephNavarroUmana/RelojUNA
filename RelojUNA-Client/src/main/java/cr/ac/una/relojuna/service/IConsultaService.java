package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import java.time.LocalDate;
import java.util.List;

public interface IConsultaService {

    //Consulta marcas usando streams, el folio es opcional, si viene null trae todos los empleados
    List<ConsultaResultadoDto> consultarMarcas(LocalDate fechaDesde, LocalDate fechaHasta, Integer folioEmpleado);
}