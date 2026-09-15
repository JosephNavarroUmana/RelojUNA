package cr.ac.una.relojuna.model;

import jakarta.json.bind.annotation.JsonbTransient;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConsultaResultadoDto {

    private ObjectProperty<Integer> folioEmpleado;
    private StringProperty nombreEmpleado;
    private ObjectProperty<LocalDate> fecha;
    private ObjectProperty<LocalTime> horaEntrada;
    private ObjectProperty<LocalTime> horaSalida;
    private ObjectProperty<Double> horasTrabajadas;

    public ConsultaResultadoDto() {
        this.folioEmpleado = new SimpleObjectProperty<>(0);
        this.nombreEmpleado = new SimpleStringProperty("");
        this.fecha = new SimpleObjectProperty<>();
        this.horaEntrada = new SimpleObjectProperty<>();
        this.horaSalida = new SimpleObjectProperty<>();
        this.horasTrabajadas = new SimpleObjectProperty<>(0.0);
    }

    public Integer getFolioEmpleado() {
        return folioEmpleado.get();
    }

    public void setFolioEmpleado(Integer folioEmpleado) {
        this.folioEmpleado.set(folioEmpleado);
    }

    public String getNombreEmpleado() {
        return nombreEmpleado.get();
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado.set(nombreEmpleado);
    }

    public LocalDate getFecha() {
        return fecha.get();
    }

    public void setFecha(LocalDate fecha) {
        this.fecha.set(fecha);
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada.get();
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada.set(horaEntrada);
    }

    public LocalTime getHoraSalida() {
        return horaSalida.get();
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida.set(horaSalida);
    }

    public Double getHorasTrabajadas() {
        return horasTrabajadas.get();
    }

    public void setHorasTrabajadas(Double horasTrabajadas) {
        this.horasTrabajadas.set(horasTrabajadas);
    }

    @JsonbTransient
    public ObjectProperty<Integer> getFolioEmpleadoProperty() {
        return folioEmpleado;
    }

    @JsonbTransient
    public StringProperty getNombreEmpleadoProperty() {
        return nombreEmpleado;
    }

    @JsonbTransient
    public ObjectProperty<LocalDate> getFechaProperty() {
        return fecha;
    }

    @JsonbTransient
    public ObjectProperty<LocalTime> getHoraEntradaProperty() {
        return horaEntrada;
    }

    @JsonbTransient
    public ObjectProperty<LocalTime> getHoraSalidaProperty() {
        return horaSalida;
    }

    @JsonbTransient
    public ObjectProperty<Double> getHorasTrabajadasProperty() {
        return horasTrabajadas;
    }
}