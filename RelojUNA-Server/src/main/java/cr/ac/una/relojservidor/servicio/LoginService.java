package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;

@Stateless
public class LoginService {

    @PersistenceContext(unitName = "RelojUNAPU")
    private EntityManager em;

    //Mensaje unico para cualquier fallo de login: folio inexistente, no-admin,
    //admin sin clave configurada, o clave incorrecta. No revelamos cual fue.
    private static final String MENSAJE_ERROR_GENERICO = "Folio o clave incorrectos.";

    public Respuesta login(String folio, String clave) {
        try {
            Empleado empleado = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.folio = :folio",
                    Empleado.class)
                    .setParameter("folio", folio)
                    .getSingleResult();

            //Solo los administradores usan este flujo de login con clave;
            //el acceso de los no-admin lo maneja otro modulo.
            if (empleado.getEsAdmin() == null || empleado.getEsAdmin() != 1) {
                return new Respuesta(false, MENSAJE_ERROR_GENERICO);
            }

            //Todo admin deberia tener clave configurada; si no la tiene,
            //es un problema de datos pero se responde igual que cualquier otro fallo.
            if (empleado.getClave() == null || empleado.getClave().isBlank()) {
                return new Respuesta(false, MENSAJE_ERROR_GENERICO);
            }

            //TODO(seguridad): la clave se guarda en texto plano en EMP_CLAVE (VARCHAR2(100)).
            //Cuando se defina un mecanismo de hashing (ej. BCrypt), reemplazar esta comparacion
            //directa por la verificacion correspondiente y migrar las claves existentes.
            if (clave == null || !clave.equals(empleado.getClave())) {
                return new Respuesta(false, MENSAJE_ERROR_GENERICO);
            }

            return new Respuesta(true, "Login exitoso", convertirADto(empleado));

        } catch (NoResultException e) {
            //Mismo mensaje generico: no revelamos si el folio existe o no.
            return new Respuesta(false, MENSAJE_ERROR_GENERICO);
        } catch (Exception e) {
            return new Respuesta(false, "Error al iniciar sesión: " + e.getMessage());
        }
    }

    private EmpleadoDto convertirADto(Empleado empleado) {
        EmpleadoDto dto = new EmpleadoDto();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellidos(empleado.getApellidos());
        dto.setCedula(empleado.getCedula());
        dto.setFechaNacimiento(empleado.getFechaNacimiento().toString());
        dto.setFolio(empleado.getFolio());
        dto.setSalarioHora(empleado.getSalarioHora());
        dto.setEsAdmin(empleado.getEsAdmin());
        //Deliberadamente NO se copia la clave: este DTO viaja al cliente
        //y la clave no tiene por que salir del servidor en la respuesta de login.
        return dto;
    }
}