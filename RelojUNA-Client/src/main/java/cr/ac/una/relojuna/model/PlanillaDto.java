package cr.ac.una.relojuna.model;

//Faltan los properties

public class PlanillaDto {

    private Integer folioEmpleado;
    private String nombreEmpleado;
    private Double horasOrdinarias;
    private Double horasExtras;
    private Double horasDobles;
    private Double salarioMensual;

    public PlanillaDto() {
    }

    public Integer getFolioEmpleado() {
        return folioEmpleado;
    }

    public void setFolioEmpleado(Integer folioEmpleado) {
        this.folioEmpleado = folioEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public Double getHorasOrdinarias() {
        return horasOrdinarias;
    }

    public void setHorasOrdinarias(Double horasOrdinarias) {
        this.horasOrdinarias = horasOrdinarias;
    }

    public Double getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Double horasExtras) {
        this.horasExtras = horasExtras;
    }

    public Double getHorasDobles() {
        return horasDobles;
    }

    public void setHorasDobles(Double horasDobles) {
        this.horasDobles = horasDobles;
    }

    public Double getSalarioMensual() {
        return salarioMensual;
    }

    public void setSalarioMensual(Double salarioMensual) {
        this.salarioMensual = salarioMensual;
    }
}