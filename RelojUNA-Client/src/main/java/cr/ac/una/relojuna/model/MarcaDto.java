package cr.ac.una.relojuna.model;

import jakarta.json.bind.annotation.JsonbTransient;
import java.time.LocalDateTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MarcaDto {

    private ObjectProperty<Integer> id;
    private StringProperty folioEmpleado;
    private StringProperty nombreEmpleado;
    private ObjectProperty<LocalDateTime> fechaHora;
    private StringProperty tipo;
    private StringProperty estado;

    public MarcaDto() {
        this.id = new SimpleObjectProperty<>(0);
        this.folioEmpleado = new SimpleStringProperty("");
        this.nombreEmpleado = new SimpleStringProperty("");
        this.fechaHora = new SimpleObjectProperty<>();
        this.tipo = new SimpleStringProperty("");
        this.estado = new SimpleStringProperty("");
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
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

    public LocalDateTime getFechaHora() {
        return fechaHora.get();
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora.set(fechaHora);
    }

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    @JsonbTransient
    public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    @JsonbTransient
    public StringProperty getFolioEmpleadoProperty() {
        return folioEmpleado;
    }

    @JsonbTransient
    public StringProperty getNombreEmpleadoProperty() {
        return nombreEmpleado;
    }

    @JsonbTransient
    public ObjectProperty<LocalDateTime> getFechaHoraProperty() {
        return fechaHora;
    }

    @JsonbTransient
    public StringProperty getTipoProperty() {
        return tipo;
    }

    @JsonbTransient
    public StringProperty getEstadoProperty() {
        return estado;
    }
}
