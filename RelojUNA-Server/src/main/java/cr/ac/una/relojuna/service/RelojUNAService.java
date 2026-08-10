package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.Empleado;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@WebService(serviceName = "RelojUNAService")
public class RelojUNAService {

    @PersistenceContext(unitName = "RelojUNAPU")
    private EntityManager em;

    @WebMethod
    public Empleado login(String folio, String clave) {
        try {
            return em.createQuery("SELECT e FROM Empleado e WHERE e.folio = :folio AND e.clave = :clave AND e.administrador = true", Empleado.class)
                    .setParameter("folio", folio)
                    .setParameter("clave", clave)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod
    public List<Empleado> getEmpleados() {
        return em.createQuery("SELECT e FROM Empleado e", Empleado.class).getResultList();
    }
    
    // Otros métodos para marcas, planillas, etc.
}
