package cr.ac.una.relojservidor.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class PlanillaDto implements Serializable {

    private Long id;
    private Integer mes;
    private Integer anio;
    private LocalDate fechaGeneracion;
    private List<DetallePlanillaDto> detalles;

    public PlanillaDto() {
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

    public List<DetallePlanillaDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePlanillaDto> detalles) {
        this.detalles = detalles;
    }
}