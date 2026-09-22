package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.util.Respuesta;
import cr.ac.una.relojuna.ws.LoginWS;
import cr.ac.una.relojuna.ws.LoginWS_Service;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Element;

public class LoginService {

    private LoginWS puerto;

    public LoginService() {
        LoginWS_Service servicioWS = new LoginWS_Service();
        puerto = servicioWS.getLoginWSPort();
    }

    public Respuesta validarLogin(String folio, String clave) {
        try {
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.login(folio, clave);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            Object resultadoCrudo = respuestaServidor.getAny();
            cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor = convertirAEmpleadoDtoServidor(resultadoCrudo);

            EmpleadoService empleadoService = new EmpleadoService();
            EmpleadoDto empleado = empleadoService.convertirAEmpleadoCliente(empleadoServidor);

            return new Respuesta(true, "", "", "Usuario", empleado);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error validando el ingreso.", "validarLogin " + ex.getMessage());
        }
    }

    private cr.ac.una.relojuna.ws.EmpleadoDto convertirAEmpleadoDtoServidor(Object resultadoCrudo) throws Exception {
        if (resultadoCrudo instanceof JAXBElement) {
            return (cr.ac.una.relojuna.ws.EmpleadoDto) ((JAXBElement<?>) resultadoCrudo).getValue();
        }

        if (resultadoCrudo instanceof cr.ac.una.relojuna.ws.EmpleadoDto) {
            return (cr.ac.una.relojuna.ws.EmpleadoDto) resultadoCrudo;
        }

        if (resultadoCrudo instanceof Element) {
            JAXBContext contexto = JAXBContext.newInstance(cr.ac.una.relojuna.ws.EmpleadoDto.class);
            Unmarshaller desempacador = contexto.createUnmarshaller();
            JAXBElement<cr.ac.una.relojuna.ws.EmpleadoDto> elemento = desempacador.unmarshal((Element) resultadoCrudo, cr.ac.una.relojuna.ws.EmpleadoDto.class);
            return elemento.getValue();
        }

        throw new Exception("No se pudo interpretar el resultado del servidor");
    }
}
