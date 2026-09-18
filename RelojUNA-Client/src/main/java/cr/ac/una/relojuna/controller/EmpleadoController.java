package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.EmpleadoService;
import cr.ac.una.relojuna.util.Respuesta;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EmpleadoController {

    @FXML
    private TextField txtBuscar, txtFolio, txtNombre, txtApellidos, txtCedula, txtSalario;
    @FXML
    private DatePicker dpFechaNac;
    @FXML
    private PasswordField txtClave;
    @FXML
    private CheckBox chkAdmin;
    @FXML
    private ImageView imgFoto;
    @FXML
    private Button btnFoto;
    @FXML
    private TableView<EmpleadoDto> tblEmpleados;
    @FXML
    private TableColumn<EmpleadoDto, String> colFolio;
    @FXML
    private TableColumn<EmpleadoDto, String> colNombre, colApellidos, colCedula;
    @FXML
    private TableColumn<EmpleadoDto, LocalDate> colFechaNac;
    @FXML
    private TableColumn<EmpleadoDto, Double> colSalario;
    @FXML
    private TableColumn<EmpleadoDto, Boolean> colAdmin;
    @FXML
    private Button btnBuscar, btnNuevo, btnGuardar, btnEliminar, btnLimpiar;
    @FXML
    private Button btnLimpiarBusqueda;
    @FXML
    private Button btnRegresar;

    private static final int EDAD_MINIMA = 18;

    private EmpleadoService empleadoService;
    private ObservableList<EmpleadoDto> listaEmpleados;

    //Id del empleado seleccionado en la tabla; null si estamos creando uno nuevo.
    //El folio ya no sirve para esto porque ahora es solo texto generado por el servidor.
    private Long idSeleccionado;

    //Bytes de la foto actualmente cargada en el formulario (nueva o existente).
    //null significa "sin foto nueva" (en edicion, el servidor no la toca).
    private byte[] fotoActual;

    @FXML
    private void initialize() {
        empleadoService = new EmpleadoService();
        listaEmpleados = FXCollections.observableArrayList();

        colFolio.setCellValueFactory(new PropertyValueFactory<>("folio"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salarioPorHora"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<>("administrador"));

        tblEmpleados.setItems(listaEmpleados);

        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                cargarFormulario(seleccionado);
            }
        });

        //La clave solo tiene sentido si es administrador: deshabilitada y vacia por defecto
        txtClave.setDisable(!chkAdmin.isSelected());
        chkAdmin.selectedProperty().addListener((obs, anterior, esAdmin) -> {
            txtClave.setDisable(!esAdmin);
            if (!esAdmin) {
                txtClave.clear();
            }
        });

        //No se puede elegir hoy, el futuro, ni una fecha que de menos de 18 anios
        dpFechaNac.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate fecha, boolean vacio) {
                super.updateItem(fecha, vacio);
                if (fecha == null) {
                    return;
                }
                boolean invalida = fecha.isAfter(LocalDate.now().minusYears(EDAD_MINIMA));
                setDisable(invalida);
                if (invalida) {
                    setStyle("-fx-background-color: #ffc0c0;");
                }
            }
        });

        cargarTabla("");
    }

    private void cargarTabla(String textoBusqueda) {
        Respuesta respuesta = empleadoService.buscarEmpleados(textoBusqueda);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        List<EmpleadoDto> empleados = (List<EmpleadoDto>) respuesta.getResultado("Empleados");
        listaEmpleados.clear();
        listaEmpleados.addAll(empleados);
    }

    private void cargarFormulario(EmpleadoDto empleado) {
        idSeleccionado = empleado.getId();

        txtFolio.setText(empleado.getFolio());
        txtNombre.setText(empleado.getNombre());
        txtApellidos.setText(empleado.getApellidos());
        txtCedula.setText(empleado.getCedula());
        dpFechaNac.setValue(empleado.getFechaNacimiento());
        txtSalario.setText(empleado.getSalarioPorHora().toString());

        //Primero el checkbox: dispara el listener que habilita/limpia txtClave
        chkAdmin.setSelected(empleado.isAdministrador());
        txtClave.setText(empleado.isAdministrador() ? empleado.getClave() : "");

        fotoActual = empleado.getFoto();
        mostrarPreviewFoto(fotoActual);
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        fotoActual = null;

        txtFolio.clear();
        txtNombre.clear();
        txtApellidos.clear();
        txtCedula.clear();
        dpFechaNac.setValue(null);
        txtSalario.clear();
        txtClave.clear();
        chkAdmin.setSelected(false);
        mostrarPreviewFoto(null);
        tblEmpleados.getSelectionModel().clearSelection();
    }

    private void mostrarPreviewFoto(byte[] foto) {
        if (foto == null || foto.length == 0) {
            imgFoto.setImage(null);
            return;
        }
        imgFoto.setImage(new Image(new ByteArrayInputStream(foto)));
    }

    @FXML
    private void handleBuscar() {
        cargarTabla(txtBuscar.getText());
    }

    @FXML
    private void handleLimpiarBusqueda() {
        txtBuscar.clear();
        cargarTabla("");
    }

    @FXML
    private void handleNuevo() {
        limpiarFormulario();
    }

    @FXML
    private void handleSeleccionarFoto() {
        FileChooser selector = new FileChooser();
        selector.setTitle("Seleccionar foto del empleado");
        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) btnFoto.getScene().getWindow();
        File archivo = selector.showOpenDialog(stage);

        if (archivo == null) {
            return;
        }

        try {
            byte[] bytesFoto = Files.readAllBytes(archivo.toPath());
            fotoActual = bytesFoto;
            mostrarPreviewFoto(bytesFoto);
        } catch (IOException ex) {
            mostrarMensaje("No se pudo leer el archivo de imagen seleccionado.");
        }
    }

    @FXML
    private void handleGuardar() {
        if (txtNombre.getText().isBlank() || txtApellidos.getText().isBlank() || txtCedula.getText().isBlank()) {
            mostrarMensaje("Nombre, apellidos y cedula son obligatorios.");
            return;
        }

        if (txtSalario.getText().isBlank()) {
            mostrarMensaje("Debe ingresar el salario por hora.");
            return;
        }

        Double salario;
        try {
            salario = Double.valueOf(txtSalario.getText());
        } catch (NumberFormatException ex) {
            mostrarMensaje("El salario debe ser un numero valido.");
            return;
        }

        LocalDate fechaNac = dpFechaNac.getValue();
        if (fechaNac == null) {
            mostrarMensaje("Debe ingresar la fecha de nacimiento.");
            return;
        }
        if (!fechaNac.isBefore(LocalDate.now())) {
            mostrarMensaje("La fecha de nacimiento no puede ser hoy ni en el futuro.");
            return;
        }
        if (fechaNac.isAfter(LocalDate.now().minusYears(EDAD_MINIMA))) {
            mostrarMensaje("El empleado debe ser mayor de edad (al menos " + EDAD_MINIMA + " anios).");
            return;
        }

        if (chkAdmin.isSelected() && (txtClave.getText() == null || txtClave.getText().isBlank())) {
            mostrarMensaje("Debe ingresar una clave para los administradores.");
            return;
        }

        EmpleadoDto empleado = new EmpleadoDto();
        //idSeleccionado es null si es un empleado nuevo; el servidor genera el folio en ese caso
        empleado.setId(idSeleccionado);

        empleado.setNombre(txtNombre.getText());
        empleado.setApellidos(txtApellidos.getText());
        empleado.setCedula(txtCedula.getText());
        empleado.setFechaNacimiento(fechaNac);
        empleado.setSalarioPorHora(salario);
        empleado.setFoto(fotoActual);
        empleado.setClave(chkAdmin.isSelected() ? txtClave.getText() : null);
        empleado.setAdministrador(chkAdmin.isSelected());

        Respuesta respuesta = empleadoService.guardarEmpleado(empleado);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }
        limpiarFormulario();
        cargarTabla(txtBuscar.getText());

    }

    @FXML
    private void handleEliminar() {
        EmpleadoDto seleccionado = tblEmpleados.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje("Debe seleccionar un empleado de la tabla.");
            return;
        }

        Respuesta respuesta = empleadoService.eliminarEmpleado(seleccionado.getId());

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        cargarTabla(txtBuscar.getText());
        limpiarFormulario();
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}