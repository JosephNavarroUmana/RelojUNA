package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.servicio.LoginService;
import cr.ac.una.relojservidor.util.Respuesta;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "LoginWS")
public class LoginWS {

    private final LoginService loginService = new LoginService();

    @WebMethod(operationName = "login")
    public Respuesta login(@WebParam(name = "folio") String folio, @WebParam(name = "clave") String clave) {
        return loginService.login(folio, clave);
    }
}