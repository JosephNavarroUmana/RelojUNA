package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MarcaDto implements Serializable {

    private Long id;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fecha;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime hora;

    private String tipo;
    private Long empleadoId;

    //Campos nuevos, se llenan solo cuando se convierte de entidad a dto, no hacen falta para guardar
    private String folioEmpleado;
    private String nombreEmpleado;

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

    public String getFolioEmpleado() { return folioEmpleado; }
    public void setFolioEmpleado(String folioEmpleado) { this.folioEmpleado = folioEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    //Conversor a texto
    static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
        @Override
        public LocalDate unmarshal(String v) {
            if (v == null || v.isBlank()) {
                return null;
            }
            return LocalDate.parse(v);
        }
        @Override
        public String marshal(LocalDate v) {
            if (v == null) {
                return null;
            }
            return v.toString();
        }
    }

    static class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
        @Override
        public LocalDateTime unmarshal(String v) {
            if (v == null || v.isBlank()) {
                return null;
            }
            return LocalDateTime.parse(v);
        }
        @Override
        public String marshal(LocalDateTime v) {
            if (v == null) {
                return null;
            }
            return v.toString();
        }
    }
}