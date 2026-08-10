package cr.ac.una.relojuna.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "RELOJ_EMPLEADO")
public class Empleado implements Serializable {

    @Id
    @Column(name = "EMP_FOLIO")
    private String folio;

    @Column(name = "EMP_NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "EMP_APELLIDOS", nullable = false)
    private String apellidos;

    @Column(name = "EMP_CEDULA", unique = true, nullable = false)
    private String cedula;

    @Column(name = "EMP_FECHA_NACIMIENTO")
    private LocalDate fechaNacimiento;

    @Lob
    @Column(name = "EMP_FOTO")
    private byte[] foto;

    @Column(name = "EMP_SALARIO_HORA")
    private Double salarioHora;

    @Column(name = "EMP_ADMINISTRADOR")
    private Boolean administrador;

    @Column(name = "EMP_CLAVE")
    private String clave;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<Marca> marcas;

    public Empleado() {}

    // Getters and Setters
    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
    public Double getSalarioHora() { return salarioHora; }
    public void setSalarioHora(Double salarioHora) { this.salarioHora = salarioHora; }
    public Boolean getAdministrador() { return administrador; }
    public void setAdministrador(Boolean administrador) { this.administrador = administrador; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public List<Marca> getMarcas() { return marcas; }
    public void setMarcas(List<Marca> marcas) { this.marcas = marcas; }
}
