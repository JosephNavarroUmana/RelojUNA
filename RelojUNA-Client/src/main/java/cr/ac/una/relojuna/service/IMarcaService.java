package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.MarcaDto;
import java.time.LocalDate;
import java.util.List;

public interface IMarcaService {

    //Registra la marca de entrada o salida de un empleado segun su folio
    MarcaDto marcar(Integer folioEmpleado);

    //Busca las marcas entre dos fechas
    List<MarcaDto> buscarMarcas(LocalDate fechaDesde, LocalDate fechaHasta);

    //Busca solo las marcas que tienen alguna inconsistencia entre dos fechas
    List<MarcaDto> buscarInconsistencias(LocalDate fechaDesde, LocalDate fechaHasta);

    //Guarda una marca nueva o actualiza una existente segun el id
    MarcaDto guardarMarca(MarcaDto marca);

    //Elimina una marca segun su id
    void eliminarMarca(Integer id);
}