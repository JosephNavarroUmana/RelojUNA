package cr.ac.una.relojservidor.ws;

import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.servicio.LoginService;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;

//Le avisamos a JAXB que EmpleadoDto puede viajar escondido dentro de Respuesta.resultado
@XmlSeeAlso({EmpleadoDto.class})
@WebService(serviceName = "LoginWS")
public class LoginWS {

    @EJB
    private LoginService loginService;

    @WebMethod(operationName = "login")
    public Respuesta login(@WebParam(name = "folio") String folio) {
        return loginService.login(folio);
    }
}