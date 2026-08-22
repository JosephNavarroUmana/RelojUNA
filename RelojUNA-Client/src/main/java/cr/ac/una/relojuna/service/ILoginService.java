package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;

public interface ILoginService {

    //Valida el folio y la clave, si son correctos retorna el empleado, si no retorna null
    EmpleadoDto validarLogin(String folio, String clave);
}