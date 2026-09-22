package cr.ac.una.relojuna.model;

import jakarta.json.bind.annotation.JsonbTransient;
import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmpleadoDto {

    private ObjectProperty<Long> id;

    private StringProperty folio;

    private StringProperty nombre;
    private StringProperty apellidos;
    private StringProperty cedula;
    private ObjectProperty<LocalDate> fechaNacimiento;
    private ObjectProperty<Double> salarioPorHora;
    private byte[] foto;
    private StringProperty clave;
    private BooleanProperty administrador;

    public EmpleadoDto() {
        this.id = new SimpleObjectProperty<>();
        this.folio = new SimpleStringProperty("");
        this.nombre = new SimpleStringProperty("");
        this.apellidos = new SimpleStringProperty("");
        this.cedula = new SimpleStringProperty("");
        this.fechaNacimiento = new SimpleObjectProperty<>();
        this.salarioPorHora = new SimpleObjectProperty<>(0.0);
        this.foto = null;
        this.clave = new SimpleStringProperty("");
        this.administrador = new SimpleBooleanProperty(false);
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getFolio() {
        return folio.get();
    }

    public void setFolio(String folio) {
        this.folio.set(folio);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getApellidos() {
        return apellidos.get();
    }

    public void setApellidos(String apellidos) {
        this.apellidos.set(apellidos);
    }

    public String getCedula() {
        return cedula.get();
    }

    public void setCedula(String cedula) {
        this.cedula.set(cedula);
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento.get();
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento.set(fechaNacimiento);
    }

    public Double getSalarioPorHora() {
        return salarioPorHora.get();
    }

    public void setSalarioPorHora(Double salarioPorHora) {
        this.salarioPorHora.set(salarioPorHora);
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getClave() {
        return clave.get();
    }

    public void setClave(String clave) {
        this.clave.set(clave);
    }

    public Boolean isAdministrador() {
        return administrador.get();
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador.set(administrador);
    }

    @JsonbTransient
    public ObjectProperty<Long> getIdProperty() {
        return id;
    }

    @JsonbTransient
    public StringProperty getFolioProperty() {
        return folio;
    }

    @JsonbTransient
    public StringProperty getNombreProperty() {
        return nombre;
    }

    @JsonbTransient
    public StringProperty getApellidosProperty() {
        return apellidos;
    }

    @JsonbTransient
    public StringProperty getCedulaProperty() {
        return cedula;
    }

    @JsonbTransient
    public ObjectProperty<LocalDate> getFechaNacimientoProperty() {
        return fechaNacimiento;
    }

    @JsonbTransient
    public ObjectProperty<Double> getSalarioPorHoraProperty() {
        return salarioPorHora;
    }

    @JsonbTransient
    public StringProperty getClaveProperty() {
        return clave;
    }

    @JsonbTransient
    public BooleanProperty getAdministradorProperty() {
        return administrador;
    }
}
