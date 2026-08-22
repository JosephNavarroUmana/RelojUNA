package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.PlanillaDto;
import java.util.List;

public interface IPlanillaService {

    //Genera la planilla de un mes y anio especifico, con las horas de todos los empleados
    List<PlanillaDto> generarPlanilla(int anio, int mes);
}