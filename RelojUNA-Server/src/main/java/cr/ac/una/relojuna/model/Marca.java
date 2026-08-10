package cr.ac.una.relojuna.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "RELOJ_MARCA")
public class Marca implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAR_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMP_FOLIO", nullable = false)
    private Empleado empleado;

    @Column(name = "MAR_ENTRADA")
    private LocalDateTime entrada;

    @Column(name = "MAR_SALIDA")
    private LocalDateTime salida;

    public Marca() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public LocalDateTime getEntrada() { return entrada; }
    public void setEntrada(LocalDateTime entrada) { this.entrada = entrada; }
    public LocalDateTime getSalida() { return salida; }
    public void setSalida(LocalDateTime salida) { this.salida = salida; }
}
