package cr.ac.una.relojuna.util;

import java.util.HashMap;
import java.util.Map;

public class Respuesta {

    //Indica si la operacion se realizo correctamente
    private boolean estado;

    //Mensaje que se le puede mostrar al usuario
    private String mensaje;

    //Mensaje interno para el log, no se le muestra al usuario
    private String mensajeInterno;

    //Mapa donde se guardan los resultados de la operacion, cada uno con un nombre
    private Map<String, Object> resultados;

    public Respuesta(boolean estado, String mensaje, String mensajeInterno) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.mensajeInterno = mensajeInterno;
        this.resultados = new HashMap<>();
    }

    public Respuesta(boolean estado, String mensaje, String mensajeInterno, String nombreResultado, Object resultado) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.mensajeInterno = mensajeInterno;
        this.resultados = new HashMap<>();
        this.resultados.put(nombreResultado, resultado);
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensajeInterno() {
        return mensajeInterno;
    }

    public void setMensajeInterno(String mensajeInterno) {
        this.mensajeInterno = mensajeInterno;
    }

    public Object getResultado(String nombre) {
        return resultados.get(nombre);
    }

    public void agregarResultado(String nombre, Object valor) {
        resultados.put(nombre, valor);
    }
}