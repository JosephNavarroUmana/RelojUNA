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

    private static final String MENSAJE_ERROR_GENERICO = "Folio o clave incorrectos.";

    public Respuesta login(String folio, String clave) {
        try {
            Empleado empleado = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.folio = :folio",
                    Empleado.class)
                    .setParameter("folio", folio)
                    .getSingleResult();

            if (empleado.getEsAdmin() == null || empleado.getEsAdmin() != 1) {
                return new Respuesta(false, MENSAJE_ERROR_GENERICO);
            }

            if (empleado.getClave() == null || empleado.getClave().isBlank()) {
                return new Respuesta(false, MENSAJE_ERROR_GENERICO);
            }

            if (clave == null || !clave.equals(empleado.getClave())) {
                return new Respuesta(false, MENSAJE_ERROR_GENERICO);
            }

            return new Respuesta(true, "Login exitoso", convertirADto(empleado));

        } catch (NoResultException e) {
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
        return dto;
    }
}