package cr.ac.una.relojuna.model;


//Faltan los properties

import java.time.LocalDate;

public class DetallePlanillaDto {

    private Integer folioEmpleado;
    private LocalDate fecha;
    private Double horasTrabajadas;
    private String tipoDia;

    public DetallePlanillaDto() {
    }

    public Integer getFolioEmpleado() {
        return folioEmpleado;
    }

    public void setFolioEmpleado(Integer folioEmpleado) {
        this.folioEmpleado = folioEmpleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(Double horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }
}