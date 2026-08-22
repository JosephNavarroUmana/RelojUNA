package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.service.IMarcaService;
import cr.ac.una.relojuna.service.ServiceFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MantenimientoMarcasController {

    @FXML
    private DatePicker dpFechaDesde, dpFechaHasta;
    @FXML
    private TableView<MarcaDto> tblMarcas;
    @FXML
    private TableColumn<MarcaDto, String> colEmpleado, colFechaHora, colTipo, colEstado;
    @FXML
    private Label lblCantInconsistencias;
    @FXML
    private Button btnFiltrar, btnVerInconsistencias, btnSiguienteInconsistencia,
                     btnAgregar, btnModificar, btnEliminar, btnCorregirInconsistencia;
    @FXML
    private Button btnRegresar;
    @FXML
    private TextField txtFolioMarca, txtHoraMarca;
    @FXML
    private DatePicker dpFechaMarca;
    @FXML
    private ComboBox<String> cmbTipoMarca;

    // Servicio de marcas, se obtiene por medio de la fabrica
    private IMarcaService marcaService;

    // Lista observable que alimenta la tabla
    private ObservableList<MarcaDto> listaMarcas;

    // Lista de inconsistencias encontradas y la posicion actual al navegar con Siguiente
    private List<MarcaDto> listaInconsistencias;
    private int indiceInconsistenciaActual;

    // Formato para mostrar la fecha y hora en la tabla
    private DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {
        marcaService = ServiceFactory.getMarcaService();
        listaMarcas = FXCollections.observableArrayList();
        indiceInconsistenciaActual = 0;

        cmbTipoMarca.setItems(FXCollections.observableArrayList("ENTRADA", "SALIDA"));

        // Enlazamos las columnas, la de fecha y hora se muestra ya formateada como texto
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFechaHora.setCellValueFactory(datos -> {
            LocalDateTime fechaHora = datos.getValue().getFechaHora();
            String texto = fechaHora.format(formatoFechaHora);
            return new javafx.beans.property.SimpleStringProperty(texto);
        });

        tblMarcas.setItems(listaMarcas);

        // Por defecto mostramos los ultimos 30 dias
        dpFechaDesde.setValue(LocalDate.now().minusDays(30));
        dpFechaHasta.setValue(LocalDate.now());

        cargarTabla();
    }

    // Trae las marcas del rango de fechas seleccionado y las pone en la tabla
    private void cargarTabla() {
        LocalDate fechaDesde = dpFechaDesde.getValue();
        LocalDate fechaHasta = dpFechaHasta.getValue();

        if (fechaDesde == null || fechaHasta == null) {
            mostrarMensaje("Debe seleccionar la fecha desde y la fecha hasta.");
            return;
        }

        List<MarcaDto> marcas = marcaService.buscarMarcas(fechaDesde, fechaHasta);
        listaMarcas.clear();
        listaMarcas.addAll(marcas);
    }

    @FXML
    private void handleFiltrar() {
        cargarTabla();
    }

    @FXML
    private void handleVerInconsistencias() {
        LocalDate fechaDesde = dpFechaDesde.getValue();
        LocalDate fechaHasta = dpFechaHasta.getValue();

        if (fechaDesde == null || fechaHasta == null) {
            mostrarMensaje("Debe seleccionar la fecha desde y la fecha hasta.");
            return;
        }

        listaInconsistencias = marcaService.buscarInconsistencias(fechaDesde, fechaHasta);
        indiceInconsistenciaActual = 0;

        lblCantInconsistencias.setText("Inconsistencias: " + listaInconsistencias.size());

        // Mostramos en la tabla solo las marcas inconsistentes
        listaMarcas.clear();
        listaMarcas.addAll(listaInconsistencias);

        if (!listaInconsistencias.isEmpty()) {
            tblMarcas.getSelectionModel().select(0);
        }
    }

    @FXML
    private void handleSiguienteInconsistencia() {
        if (listaInconsistencias == null || listaInconsistencias.isEmpty()) {
            mostrarMensaje("Primero debe presionar Ver Inconsistencias.");
            return;
        }

        indiceInconsistenciaActual = indiceInconsistenciaActual + 1;

        // Si llegamos al final de la lista, volvemos a empezar desde el principio
        if (indiceInconsistenciaActual >= listaInconsistencias.size()) {
            indiceInconsistenciaActual = 0;
        }

        tblMarcas.getSelectionModel().select(indiceInconsistenciaActual);
        tblMarcas.scrollTo(indiceInconsistenciaActual);
    }

    @FXML
    private void handleAgregar() {
        MarcaDto marcaNueva = leerFormulario(null);

        if (marcaNueva == null) {
            return;
        }

        marcaService.guardarMarca(marcaNueva);
        cargarTabla();
        limpiarFormulario();
    }

    @FXML
    private void handleModificar() {
        MarcaDto seleccionada = tblMarcas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Debe seleccionar una marca de la tabla.");
            return;
        }

        MarcaDto marcaModificada = leerFormulario(seleccionada.getId());

        if (marcaModificada == null) {
            return;
        }

        marcaService.guardarMarca(marcaModificada);
        cargarTabla();
        limpiarFormulario();
    }

    @FXML
    private void handleEliminar() {
        MarcaDto seleccionada = tblMarcas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Debe seleccionar una marca de la tabla.");
            return;
        }

        marcaService.eliminarMarca(seleccionada.getId());
        cargarTabla();
        limpiarFormulario();
    }

    @FXML
    private void handleCorregirInconsistencia() {
        MarcaDto seleccionada = tblMarcas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Debe seleccionar una marca de la tabla.");
            return;
        }

        // Invertimos el tipo de la marca, si era entrada pasa a salida y viceversa
        if (seleccionada.getTipo().equals("ENTRADA")) {
            seleccionada.setTipo("SALIDA");
        } else {
            seleccionada.setTipo("ENTRADA");
        }

        seleccionada.setEstado("OK");
        marcaService.guardarMarca(seleccionada);

        // Volvemos a calcular las inconsistencias para actualizar la lista
        handleVerInconsistencias();
    }

    // Lee los datos del formulario y arma un MarcaDto, retorna null si hay error de validacion
    private MarcaDto leerFormulario(Integer idExistente) {
        String folioTexto = txtFolioMarca.getText();
        LocalDate fecha = dpFechaMarca.getValue();
        String horaTexto = txtHoraMarca.getText();
        String tipo = cmbTipoMarca.getValue();

        if (folioTexto == null || folioTexto.isBlank()) {
            mostrarMensaje("Debe ingresar el folio del empleado.");
            return null;
        }

        if (fecha == null) {
            mostrarMensaje("Debe seleccionar la fecha.");
            return null;
        }

        if (horaTexto == null || horaTexto.isBlank()) {
            mostrarMensaje("Debe ingresar la hora en formato HH:mm.");
            return null;
        }

        if (tipo == null) {
            mostrarMensaje("Debe seleccionar el tipo de marca.");
            return null;
        }

        Integer folio;
        try {
            folio = Integer.valueOf(folioTexto);
        } catch (NumberFormatException ex) {
            mostrarMensaje("El folio debe ser un numero.");
            return null;
        }

        LocalTime hora;
        try {
            hora = LocalTime.parse(horaTexto);
        } catch (Exception ex) {
            mostrarMensaje("La hora debe tener el formato HH:mm, por ejemplo 08:00.");
            return null;
        }

        MarcaDto marca = new MarcaDto();
        marca.setId(idExistente);
        marca.setFolioEmpleado(folio);
        marca.setNombreEmpleado("Empleado " + folio);
        marca.setFechaHora(LocalDateTime.of(fecha, hora));
        marca.setTipo(tipo);
        marca.setEstado("OK");

        return marca;
    }

    private void limpiarFormulario() {
        txtFolioMarca.clear();
        dpFechaMarca.setValue(null);
        txtHoraMarca.clear();
        cmbTipoMarca.setValue(null);
        tblMarcas.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }
}