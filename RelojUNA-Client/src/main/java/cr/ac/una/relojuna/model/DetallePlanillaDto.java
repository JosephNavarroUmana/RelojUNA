package cr.ac.una.relojuna.model;

import jakarta.json.bind.annotation.JsonbTransient;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DetallePlanillaDto {

    private ObjectProperty<Integer> folioEmpleado;
    private ObjectProperty<LocalDate> fecha;
    private ObjectProperty<Double> horasTrabajadas;
    private StringProperty tipoDia;

    public DetallePlanillaDto() {
        this.folioEmpleado = new SimpleObjectProperty<>(0);
        this.fecha = new SimpleObjectProperty<>();
        this.horasTrabajadas = new SimpleObjectProperty<>(0.0);
        this.tipoDia = new SimpleStringProperty("");
    }

    public Integer getFolioEmpleado() {
        return folioEmpleado.get();
    }

    public void setFolioEmpleado(Integer folioEmpleado) {
        this.folioEmpleado.set(folioEmpleado);
    }

    public LocalDate getFecha() {
        return fecha.get();
    }

    public void setFecha(LocalDate fecha) {
        this.fecha.set(fecha);
    }

    public Double getHorasTrabajadas() {
        return horasTrabajadas.get();
    }

    public void setHorasTrabajadas(Double horasTrabajadas) {
        this.horasTrabajadas.set(horasTrabajadas);
    }

    public String getTipoDia() {
        return tipoDia.get();
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia.set(tipoDia);
    }

    @JsonbTransient
    public ObjectProperty<Integer> getFolioEmpleadoProperty() {
        return folioEmpleado;
    }

    @JsonbTransient
    public ObjectProperty<LocalDate> getFechaProperty() {
        return fecha;
    }

    @JsonbTransient
    public ObjectProperty<Double> getHorasTrabajadasProperty() {
        return horasTrabajadas;
    }

    @JsonbTransient
    public StringProperty getTipoDiaProperty() {
        return tipoDia;
    }
}