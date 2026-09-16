package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

//Esta clase es solo un envoltorio para poder mandar una lista de marcas por SOAP
//JAXB no puede mandar una lista sola, necesita una clase conocida que la contenga
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaMarcaDto implements Serializable {

    private List<MarcaDto> marcas;

    public ListaMarcaDto() {
    }

    public ListaMarcaDto(List<MarcaDto> marcas) {
        this.marcas = marcas;
    }

    public List<MarcaDto> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<MarcaDto> marcas) {
        this.marcas = marcas;
    }
}