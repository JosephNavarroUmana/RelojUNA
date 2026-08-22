package cr.ac.una.relojservidor.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerHelper {

    private static final EntityManagerHelper SINGLETON = new EntityManagerHelper();
    private static EntityManagerFactory emf;
    private static EntityManager em;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("RelojUNAPU");
            em = emf.createEntityManager();
        } catch (ExceptionInInitializerError e) {
            throw e;
        }
    }

    public static EntityManagerHelper getInstance() {
        return SINGLETON;
    }

    public static EntityManager getManager() {
        if (em == null) {
            emf = Persistence.createEntityManagerFactory("RelojUNAPU");
            em = emf.createEntityManager();
        }
        return em;
    }
}