package cr.ac.una.relojuna.util;

import cr.ac.una.relojuna.model.EmpleadoDto;

public class SesionTemporal {

    //Instancia unica de la clase(patron singleton)
    private static SesionTemporal instancia;

    //Empleado que inicio sesion actualmente
    private EmpleadoDto empleadoActual;

    private SesionTemporal() {
    }

    public static SesionTemporal getInstancia() {
        if (instancia == null) {
            instancia = new SesionTemporal();
        }
        return instancia;
    }

    public EmpleadoDto getEmpleadoActual() {
        return empleadoActual;
    }

    public void setEmpleadoActual(EmpleadoDto empleado) {
        this.empleadoActual = empleado;
    }

    //Limpia la sesion cuando el usuario cierra sesion
    public void cerrarSesion() {
        this.empleadoActual = null;
    }
}