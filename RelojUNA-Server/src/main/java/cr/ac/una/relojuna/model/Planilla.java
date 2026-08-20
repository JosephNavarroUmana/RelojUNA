package cr.ac.una.relojservidor.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PLANILLA")
public class Planilla implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLA_ID")
    private Long id;

    @Column(name = "PLA_MES", nullable = false)
    private Integer mes;

    @Column(name = "PLA_ANIO", nullable = false)
    private Integer anio;

    @Column(name = "PLA_FECHA_GENERACION", nullable = false)
    private LocalDate fechaGeneracion;

    @OneToMany(mappedBy = "planilla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePlanilla> detalles;

    public Planilla() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public List<DetallePlanilla> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePlanilla> detalles) {
        this.detalles = detalles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Planilla)) return false;
        Planilla planilla = (Planilla) o;
        return id != null && id.equals(planilla.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}