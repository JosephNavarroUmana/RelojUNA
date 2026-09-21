package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaConsultaFilaDto implements Serializable {

    private List<ConsultaFilaDto> filas;

    public ListaConsultaFilaDto() {
    }

    public ListaConsultaFilaDto(List<ConsultaFilaDto> filas) {
        this.filas = filas;
    }

    public List<ConsultaFilaDto> getFilas() {
        return filas;
    }

    public void setFilas(List<ConsultaFilaDto> filas) {
        this.filas = filas;
    }
}