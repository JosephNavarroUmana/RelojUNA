package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

//Envoltorio para poder mandar una lista de filas de planilla por SOAP
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaPlanillaFilaDto implements Serializable {

    private List<PlanillaFilaDto> filas;

    public ListaPlanillaFilaDto() {
    }

    public ListaPlanillaFilaDto(List<PlanillaFilaDto> filas) {
        this.filas = filas;
    }

    public List<PlanillaFilaDto> getFilas() {
        return filas;
    }

    public void setFilas(List<PlanillaFilaDto> filas) {
        this.filas = filas;
    }
}