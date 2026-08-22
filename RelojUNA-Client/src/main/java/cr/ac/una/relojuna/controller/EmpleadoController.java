package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.IEmpleadoService;
import cr.ac.una.relojuna.service.ServiceFactory;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EmpleadoController {

    @FXML
    private TextField txtBuscar, txtFolio, txtNombre, txtApellidos, txtCedula, txtSalario, txtFoto;
    @FXML
    private DatePicker dpFechaNac;
    @FXML
    private PasswordField txtClave;
    @FXML
    private CheckBox chkAdmin;
    @FXML
    private TableView<EmpleadoDto> tblEmpleados;
    @FXML
    private TableColumn<EmpleadoDto, Integer> colFolio;
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

    // Servicio de empleados, se obtiene por medio de la fabrica
    private IEmpleadoService empleadoService;

    // Lista observable que alimenta la tabla
    private ObservableList<EmpleadoDto> listaEmpleados;

    @FXML
    private void initialize() {
        empleadoService = ServiceFactory.getEmpleadoService();
        listaEmpleados = FXCollections.observableArrayList();

        // Enlazamos cada columna con el atributo correspondiente del EmpleadoDto
        colFolio.setCellValueFactory(new PropertyValueFactory<>("folio"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salarioPorHora"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<>("administrador"));

        tblEmpleados.setItems(listaEmpleados);

        // Cuando el usuario selecciona una fila, llenamos el formulario con esos datos
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                cargarFormulario(seleccionado);
            }
        });

        cargarTabla("");
    }

    // Trae los empleados desde el servicio y los pone en la tabla
    private void cargarTabla(String textoBusqueda) {
        List<EmpleadoDto> empleados = empleadoService.buscarEmpleados(textoBusqueda);
        listaEmpleados.clear();
        listaEmpleados.addAll(empleados);
    }

    // Llena los campos del formulario con los datos de un empleado
    private void cargarFormulario(EmpleadoDto empleado) {
        txtFolio.setText(empleado.getFolio().toString());
        txtNombre.setText(empleado.getNombre());
        txtApellidos.setText(empleado.getApellidos());
        txtCedula.setText(empleado.getCedula());
        dpFechaNac.setValue(empleado.getFechaNacimiento());
        txtSalario.setText(empleado.getSalarioPorHora().toString());
        txtFoto.setText(empleado.getFoto());
        txtClave.setText(empleado.getClave());
        chkAdmin.setSelected(empleado.isAdministrador());
    }

    // Limpia todos los campos del formulario
    private void limpiarFormulario() {
        txtFolio.clear();
        txtNombre.clear();
        txtApellidos.clear();
        txtCedula.clear();
        dpFechaNac.setValue(null);
        txtSalario.clear();
        txtFoto.clear();
        txtClave.clear();
        chkAdmin.setSelected(false);
        tblEmpleados.getSelectionModel().clearSelection();
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
    private void handleGuardar() {
        // Validamos los campos obligatorios antes de guardar
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

        EmpleadoDto empleado = new EmpleadoDto();

        // Si el campo folio tiene algo, es una modificacion, si no, es un empleado nuevo
        if (!txtFolio.getText().isBlank()) {
            empleado.setFolio(Integer.valueOf(txtFolio.getText()));
        }

        empleado.setNombre(txtNombre.getText());
        empleado.setApellidos(txtApellidos.getText());
        empleado.setCedula(txtCedula.getText());
        empleado.setFechaNacimiento(dpFechaNac.getValue());
        empleado.setSalarioPorHora(salario);
        empleado.setFoto(txtFoto.getText());
        empleado.setClave(txtClave.getText());
        empleado.setAdministrador(chkAdmin.isSelected());

        empleadoService.guardarEmpleado(empleado);

        cargarTabla(txtBuscar.getText());
        limpiarFormulario();
    }

    @FXML
    private void handleEliminar() {
        EmpleadoDto seleccionado = tblEmpleados.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje("Debe seleccionar un empleado de la tabla.");
            return;
        }

        empleadoService.eliminarEmpleado(seleccionado.getFolio());
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

    // Muestra una ventana con un mensaje al usuario
    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}