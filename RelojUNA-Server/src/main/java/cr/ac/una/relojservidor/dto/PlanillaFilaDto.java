package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

//Representa una fila del reporte de planilla, un empleado con sus horas y salario del mes
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanillaFilaDto implements Serializable {

    private String folioEmpleado;
    private String nombreEmpleado;
    private Double horasOrdinarias;
    private Double horasExtras;
    private Double horasDobles;
    private Double salarioMensual;

    public PlanillaFilaDto() {
    }

    public String getFolioEmpleado() { return folioEmpleado; }
    
    public void setFolioEmpleado(String folioEmpleado) { this.folioEmpleado = folioEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public Double getHorasOrdinarias() { return horasOrdinarias; }
    public void setHorasOrdinarias(Double horasOrdinarias) { this.horasOrdinarias = horasOrdinarias; }

    public Double getHorasExtras() { return horasExtras; }
    public void setHorasExtras(Double horasExtras) { this.horasExtras = horasExtras; }

    public Double getHorasDobles() { return horasDobles; }
    public void setHorasDobles(Double horasDobles) { this.horasDobles = horasDobles; }

    public Double getSalarioMensual() { return salarioMensual; }
    public void setSalarioMensual(Double salarioMensual) { this.salarioMensual = salarioMensual; }
}