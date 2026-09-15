package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class MarcaDto implements Serializable {

    private Long id;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fecha;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime hora;

    private String tipo;
    private Long empleadoId;

    public MarcaDto() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalDateTime getHora() { return hora; }
    public void setHora(LocalDateTime hora) { this.hora = hora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Long getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Long empleadoId) { this.empleadoId = empleadoId; }

    // --- Adapters anidados ---
    static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
        @Override
        public LocalDate unmarshal(String v) {
            return (v == null || v.isBlank()) ? null : LocalDate.parse(v);
        }
        @Override
        public String marshal(LocalDate v) {
            return (v == null) ? null : v.toString();
        }
    }

    static class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
        @Override
        public LocalDateTime unmarshal(String v) {
            return (v == null || v.isBlank()) ? null : LocalDateTime.parse(v);
        }
        @Override
        public String marshal(LocalDateTime v) {
            return (v == null) ? null : v.toString();
        }
    }
}