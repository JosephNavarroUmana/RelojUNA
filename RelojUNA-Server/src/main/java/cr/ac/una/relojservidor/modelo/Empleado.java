package cr.ac.una.relojservidor.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "EMPLEADO")
public class Empleado implements Serializable {

   @Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_empleado")
@SequenceGenerator(name = "seq_empleado", sequenceName = "SEQ_EMP_ID", allocationSize = 1)
@Column(name = "EMP_ID")
private Long id;
//    private Long id;

    @Column(name = "EMP_NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "EMP_APELLIDOS", length = 80, nullable = false)
    private String apellidos;

    @Column(name = "EMP_CEDULA", length = 20, nullable = false)
    private String cedula;

    @Column(name = "EMP_FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @Lob
    @Column(name = "EMP_FOTO")
    private byte[] foto;

    @Column(name = "EMP_FOLIO", length = 20, nullable = false, unique = true)
    private String folio;

    @Column(name = "EMP_SALARIO_HORA", nullable = false)
    private Double salarioHora;

    @Column(name = "EMP_ES_ADMIN", nullable = false)
    private Integer esAdmin;

    @Column(name = "EMP_CLAVE", length = 100)
    private String clave;

    // --- Constructores ---
    public Empleado() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado)) return false;
        Empleado empleado = (Empleado) o;
        return id != null && id.equals(empleado.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}