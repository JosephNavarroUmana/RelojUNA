package cr.ac.una.relojuna.model;

import jakarta.json.bind.annotation.JsonbTransient;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlanillaDto {

    private ObjectProperty<String> folioEmpleado;
    private StringProperty nombreEmpleado;
    private ObjectProperty<Double> horasOrdinarias;
    private ObjectProperty<Double> horasExtras;
    private ObjectProperty<Double> horasDobles;
    private ObjectProperty<Double> salarioMensual;

    public PlanillaDto() {
        this.folioEmpleado = new SimpleObjectProperty<>("");
        this.nombreEmpleado = new SimpleStringProperty("");
        this.horasOrdinarias = new SimpleObjectProperty<>(0.0);
        this.horasExtras = new SimpleObjectProperty<>(0.0);
        this.horasDobles = new SimpleObjectProperty<>(0.0);
        this.salarioMensual = new SimpleObjectProperty<>(0.0);
    }

    public String getFolioEmpleado() {
        return folioEmpleado.get();
    }

    public void setFolioEmpleado(String folioEmpleado) {
        this.folioEmpleado.set(folioEmpleado);
    }

    public String getNombreEmpleado() {
        return nombreEmpleado.get();
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado.set(nombreEmpleado);
    }

    public Double getHorasOrdinarias() {
        return horasOrdinarias.get();
    }

    public void setHorasOrdinarias(Double horasOrdinarias) {
        this.horasOrdinarias.set(horasOrdinarias);
    }

    public Double getHorasExtras() {
        return horasExtras.get();
    }

    public void setHorasExtras(Double horasExtras) {
        this.horasExtras.set(horasExtras);
    }

    public Double getHorasDobles() {
        return horasDobles.get();
    }

    public void setHorasDobles(Double horasDobles) {
        this.horasDobles.set(horasDobles);
    }

    public Double getSalarioMensual() {
        return salarioMensual.get();
    }

    public void setSalarioMensual(Double salarioMensual) {
        this.salarioMensual.set(salarioMensual);
    }

    @JsonbTransient
    public ObjectProperty<String> getFolioEmpleadoProperty() {
        return folioEmpleado;
    }

    @JsonbTransient
    public StringProperty getNombreEmpleadoProperty() {
        return nombreEmpleado;
    }

    @JsonbTransient
    public ObjectProperty<Double> getHorasOrdinariasProperty() {
        return horasOrdinarias;
    }

    @JsonbTransient
    public ObjectProperty<Double> getHorasExtrasProperty() {
        return horasExtras;
    }

    @JsonbTransient
    public ObjectProperty<Double> getHorasDoblesProperty() {
        return horasDobles;
    }

    @JsonbTransient
    public ObjectProperty<Double> getSalarioMensualProperty() {
        return salarioMensual;
    }
}
