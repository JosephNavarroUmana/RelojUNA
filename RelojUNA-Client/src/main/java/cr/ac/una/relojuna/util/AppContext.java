package cr.ac.una.relojuna.util;

import java.util.HashMap;

public class AppContext {

    private static AppContext instancia;
    private static HashMap<String, Object> contexto = new HashMap<>();

    private AppContext() {
    }

    public static AppContext getInstance() {
        if (instancia == null) {
            instancia = new AppContext();
        }
        return instancia;
    }

    public Object get(String nombre) {
        return contexto.get(nombre);
    }

    public void set(String nombre, Object valor) {
        contexto.put(nombre, valor);
    }

    public void delete(String nombre) {
        contexto.remove(nombre);
    }
}
