package cr.ac.una.relojservidor.util;

import java.io.Serializable;

public class Respuesta implements Serializable {

    private boolean exito;
    private String mensaje;
    private Object resultado;

    public Respuesta() {
    }

    public Respuesta(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public Respuesta(boolean exito, String mensaje, Object resultado) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.resultado = resultado;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getResultado() {
        return resultado;
    }

    public void setResultado(Object resultado) {
        this.resultado = resultado;
    }
}