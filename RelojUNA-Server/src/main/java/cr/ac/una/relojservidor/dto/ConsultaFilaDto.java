package cr.ac.una.relojservidor.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

//Representa una fila del reporte de consultas, un empleado en un dia especifico
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsultaFilaDto implements Serializable {

    private String folioEmpleado;
    private String nombreEmpleado;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fecha;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaEntrada;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaSalida;

    private Double horasTrabajadas;

    public ConsultaFilaDto() {
    }

    public String getFolioEmpleado() { return folioEmpleado; }
    public void setFolioEmpleado(String folioEmpleado) { this.folioEmpleado = folioEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }

    public Double getHorasTrabajadas() { return horasTrabajadas; }
    public void setHorasTrabajadas(Double horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }

    //Adaptador para que jaxb pueda mandar la fecha como texto
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

    //Adaptador para que jaxb pueda mandar la hora como texto
    static class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
        @Override
        public LocalTime unmarshal(String v) {
            if (v == null || v.isBlank()) {
                return null;
            }
            return LocalTime.parse(v);
        }
        @Override
        public String marshal(LocalTime v) {
            if (v == null) {
                return null;
            }
            return v.toString();
        }
    }
}