package cr.ac.una.relojservidor.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MarcaDto implements Serializable {

    private Long id;
    private LocalDate fecha;
    private LocalDateTime hora;
    private String tipo;
    private Long empleadoId;

    public MarcaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }
}