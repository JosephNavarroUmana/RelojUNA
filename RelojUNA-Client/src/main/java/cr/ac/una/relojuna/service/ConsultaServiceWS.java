package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import java.time.LocalDate;
import java.util.List;

public class ConsultaServiceWS implements IConsultaService {

    @Override
    public List<ConsultaResultadoDto> consultarMarcas(LocalDate fechaDesde, LocalDate fechaHasta, Integer folioEmpleado) {
        //Pendiente, se implementa cuando este listo el servicio real
        return null;
    }
}