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

    //Ya no se pide clave, solo el folio y que el empleado sea administrador
    public Respuesta login(String folio) {
        try {
            Empleado empleado = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.folio = :folio AND e.esAdmin = 1",
                    Empleado.class)
                    .setParameter("folio", folio)
                    .getSingleResult();

            return new Respuesta(true, "Login exitoso", convertirADto(empleado));

        } catch (NoResultException e) {
            return new Respuesta(false, "Folio incorrecto, o el empleado no es administrador");
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