package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

//Envoltorio para poder mandar un archivo (excel o pdf) dentro de Respuesta.resultado
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ArchivoDto implements Serializable {

    private byte[] contenido;

    public ArchivoDto() {
    }

    public ArchivoDto(byte[] contenido) {
        this.contenido = contenido;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
}