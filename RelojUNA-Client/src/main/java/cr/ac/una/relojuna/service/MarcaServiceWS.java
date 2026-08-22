package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.MarcaDto;
import java.time.LocalDate;
import java.util.List;

public class MarcaServiceWS implements IMarcaService {

    @Override
    public MarcaDto marcar(Integer folioEmpleado) {
        //Pendiente, se implementa cuando este listo el servicio real
        return null;
    }

    @Override
    public List<MarcaDto> buscarMarcas(LocalDate fechaDesde, LocalDate fechaHasta) {
        //Pendiente, se implementa cuando este listo el servicio real
        return null;
    }

    @Override
    public List<MarcaDto> buscarInconsistencias(LocalDate fechaDesde, LocalDate fechaHasta) {
        //Pendiente, se implementa cuando este listo el servicio real
        return null;
    }

    @Override
    public MarcaDto guardarMarca(MarcaDto marca) {
        //Pendiente, se implementa cuando este listo el servicio real
        return null;
    }

    @Override
    public void eliminarMarca(Integer id) {
        //Pendiente, se implementa cuando este listo el servicio real
    }
}