package cr.ac.una.relojuna.model;

import java.time.LocalDateTime;

//Faltan los properties

public class MarcaDto {

    private Integer id;
    private Integer folioEmpleado;
    private String nombreEmpleado;
    private LocalDateTime fechaHora;
    private String tipo;
    private String estado;

    public MarcaDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}