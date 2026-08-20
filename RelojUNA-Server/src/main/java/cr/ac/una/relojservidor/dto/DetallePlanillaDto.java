package cr.ac.una.relojservidor.dto;

import java.io.Serializable;

public class DetallePlanillaDto implements Serializable {

    private Long id;
    private Integer horasOrdinarias;
    private Integer horasExtras;
    private Integer horasDobles;
    private Double salarioTotal;
    private Long planillaId;
    private Long empleadoId;

    public DetallePlanillaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHorasOrdinarias() {
        return horasOrdinarias;
    }

    public void setHorasOrdinarias(Integer horasOrdinarias) {
        this.horasOrdinarias = horasOrdinarias;
    }

    public Integer getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Integer horasExtras) {
        this.horasExtras = horasExtras;
    }

    public Integer getHorasDobles() {
        return horasDobles;
    }

    public void setHorasDobles(Integer horasDobles) {
        this.horasDobles = horasDobles;
    }

    public Double getSalarioTotal() {
        return salarioTotal;
    }

    public void setSalarioTotal(Double salarioTotal) {
        this.salarioTotal = salarioTotal;
    }

    public Long getPlanillaId() {
        return planillaId;
    }

    public void setPlanillaId(Long planillaId) {
        this.planillaId = planillaId;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }
}