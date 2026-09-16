package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

//Esta clase es solo un envoltorio para poder mandar una lista de empleados por SOAP
//JAXB no puede mandar una lista sola, necesita una clase conocida que la contenga
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaEmpleadoDto implements Serializable {

    private List<EmpleadoDto> empleados;

    public ListaEmpleadoDto() {
    }

    public ListaEmpleadoDto(List<EmpleadoDto> empleados) {
        this.empleados = empleados;
    }

    public List<EmpleadoDto> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<EmpleadoDto> empleados) {
        this.empleados = empleados;
    }
}