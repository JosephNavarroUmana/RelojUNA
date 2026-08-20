package cr.ac.una.relojservidor.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class EmpleadoDto implements Serializable {

    private Long id;
    private String nombre;
    private String apellidos;
    private String cedula;
    private LocalDate fechaNacimiento;
    private byte[] foto;
    private String folio;
    private Double salarioHora;
    private Integer esAdmin;
    private String clave;

    // --- Constructores ---
    public EmpleadoDto() {
    }

    // --- Getters y Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Double getSalarioHora() {
        return salarioHora;
    }

    public void setSalarioHora(Double salarioHora) {
        this.salarioHora = salarioHora;
    }

    public Integer getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(Integer esAdmin) {
        this.esAdmin = esAdmin;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}