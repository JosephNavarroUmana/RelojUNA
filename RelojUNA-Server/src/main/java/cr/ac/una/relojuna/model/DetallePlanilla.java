package cr.ac.una.relojservidor.modelo;

import cr.ac.una.relojuna.model.Planilla;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DETALLE_PLANILLA")
public class DetallePlanilla implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DPL_ID")
    private Long id;

    @Column(name = "DPL_HORAS_ORDINARIAS", nullable = false)
    private Integer horasOrdinarias;

    @Column(name = "DPL_HORAS_EXTRAS", nullable = false)
    private Integer horasExtras;

    @Column(name = "DPL_HORAS_DOBLES", nullable = false)
    private Integer horasDobles;

    @Column(name = "DPL_SALARIO_TOTAL", nullable = false)
    private Double salarioTotal;

    @ManyToOne
    @JoinColumn(name = "DPL_PLA_ID", nullable = false)
    private Planilla planilla;

    @ManyToOne
    @JoinColumn(name = "DPL_EMP_ID", nullable = false)
    private Empleado empleado;

    public DetallePlanilla() {
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

    public Planilla getPlanilla() {
        return planilla;
    }

    public void setPlanilla(Planilla planilla) {
        this.planilla = planilla;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetallePlanilla)) return false;
        DetallePlanilla that = (DetallePlanilla) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}