package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.util.Respuesta;
import cr.ac.una.relojuna.ws.LoginWS;
import cr.ac.una.relojuna.ws.LoginWS_Service;
import jakarta.xml.bind.JAXBElement;

public class LoginService {

    private LoginWS puerto;

    public LoginService() {
        LoginWS_Service servicioWS = new LoginWS_Service();
        puerto = servicioWS.getLoginWSPort();
    }

    public Respuesta validarLogin(String folio) {
        try {
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.login(folio);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            Object resultadoCrudo = respuestaServidor.getAny();
            cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor;

            if (resultadoCrudo instanceof JAXBElement) {
                empleadoServidor = (cr.ac.una.relojuna.ws.EmpleadoDto) ((JAXBElement<?>) resultadoCrudo).getValue();
            } else {
                empleadoServidor = (cr.ac.una.relojuna.ws.EmpleadoDto) resultadoCrudo;
            }

            EmpleadoService empleadoService = new EmpleadoService();
            EmpleadoDto empleado = empleadoService.convertirAEmpleadoCliente(empleadoServidor);

            return new Respuesta(true, "", "", "Usuario", empleado);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error validando el ingreso.", "validarLogin " + ex.getMessage());
        }
    }
}