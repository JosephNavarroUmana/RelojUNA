package cr.ac.una.relojservidor.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "MARCA")
public class Marca implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAR_ID")
    private Long id;

    @Column(name = "MAR_FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "MAR_HORA", nullable = false)
    private LocalDateTime hora;

    @Column(name = "MAR_TIPO", length = 10, nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "MAR_EMP_ID", nullable = false)
    private Empleado empleado;

    public Marca() {
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

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Marca)) return false;
        Marca marca = (Marca) o;
        return id != null && id.equals(marca.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}