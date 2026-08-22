package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.util.EntityManagerHelper;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class LoginService {

    public Respuesta login(String folio, String clave) {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            Empleado empleado = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.folio = :folio AND e.clave = :clave AND e.esAdmin = 1",
                    Empleado.class)
                    .setParameter("folio", folio)
                    .setParameter("clave", clave)
                    .getSingleResult();

            return new Respuesta(true, "Login exitoso", empleado);

        } catch (NoResultException e) {
            return new Respuesta(false, "Folio o clave incorrectos, o el empleado no es administrador");
        } catch (Exception e) {
            return new Respuesta(false, "Error al iniciar sesión: " + e.getMessage());
        }
    }
}