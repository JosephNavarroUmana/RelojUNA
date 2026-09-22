package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.service.MarcaService;
import cr.ac.una.relojuna.util.Respuesta;
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
import javafx.scene.control.TableRow;
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
    private Button btnRegresar;
    @FXML
    private TextField txtFolioMarca, txtHoraMarca;
    @FXML
    private DatePicker dpFechaMarca;
    @FXML
    private ComboBox<String> cmbTipoMarca;
    private MarcaService marcaService;
    private ObservableList<MarcaDto> listaMarcas;
    private List<MarcaDto> listaInconsistencias;
    private int indiceInconsistenciaActual;
    private DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private void initialize() {
        marcaService = new MarcaService();
        listaMarcas = FXCollections.observableArrayList();
        indiceInconsistenciaActual = 0;

        cmbTipoMarca.setItems(FXCollections.observableArrayList("ENTRADA", "SALIDA"));

        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFechaHora.setCellValueFactory(datos -> {
            LocalDateTime fechaHora = datos.getValue().getFechaHora();
            String texto = fechaHora.format(formatoFechaHora);
            return new javafx.beans.property.SimpleStringProperty(texto);
        });

        tblMarcas.setItems(listaMarcas);

        tblMarcas.setRowFactory(tv -> new TableRow<MarcaDto>() {
            @Override
            protected void updateItem(MarcaDto marca, boolean vacio) {
                super.updateItem(marca, vacio);
                getStyleClass().remove("fila-inconsistente");
                if (!vacio && marca != null && "Inconsistente".equals(marca.getEstado())) {
                    getStyleClass().add("fila-inconsistente");
                }
            }
        });

        tblMarcas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionada) -> {
            if (seleccionada != null) {
                cargarFormulario(seleccionada);
            }
        });

        dpFechaDesde.setValue(LocalDate.now().minusDays(30));
        dpFechaHasta.setValue(LocalDate.now());

        cargarTabla();
    }

    private void cargarTabla() {
        LocalDate fechaDesde = dpFechaDesde.getValue();
        LocalDate fechaHasta = dpFechaHasta.getValue();

        if (fechaDesde == null || fechaHasta == null) {
            mostrarMensaje("Debe seleccionar la fecha desde y la fecha hasta.");
            return;
        }

        Respuesta respuesta = marcaService.buscarMarcas(fechaDesde, fechaHasta);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        List<MarcaDto> marcas = (List<MarcaDto>) respuesta.getResultado("Marcas");

        marcarInconsistencias(marcas, fechaDesde, fechaHasta);

        listaMarcas.clear();
        listaMarcas.addAll(marcas);
    }

    private void marcarInconsistencias(List<MarcaDto> marcas, LocalDate fechaDesde, LocalDate fechaHasta) {
        Respuesta respuestaInconsistencias = marcaService.buscarInconsistencias(fechaDesde, fechaHasta);

        if (!respuestaInconsistencias.getEstado()) {
            return;
        }

        List<MarcaDto> inconsistentes = (List<MarcaDto>) respuestaInconsistencias.getResultado("Marcas");

        for (MarcaDto marca : marcas) {
            for (MarcaDto inconsistente : inconsistentes) {
                if (marca.getId().equals(inconsistente.getId())) {
                    marca.setEstado("Inconsistente");
                }
            }
        }
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

        Respuesta respuesta = marcaService.buscarInconsistencias(fechaDesde, fechaHasta);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        listaInconsistencias = (List<MarcaDto>) respuesta.getResultado("Marcas");
        for (MarcaDto m : listaInconsistencias) {
            m.setEstado("Inconsistente");
        }
        indiceInconsistenciaActual = 0;

        lblCantInconsistencias.setText("Inconsistencias: " + listaInconsistencias.size());

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

        if (indiceInconsistenciaActual >= listaInconsistencias.size()) {
            indiceInconsistenciaActual = 0;
        }

        tblMarcas.getSelectionModel().select(indiceInconsistenciaActual);
        tblMarcas.scrollTo(indiceInconsistenciaActual);
    }

    @FXML
    private void handleEliminar() {
        MarcaDto seleccionada = tblMarcas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Debe seleccionar una marca de la tabla.");
            return;
        }

        Respuesta respuesta = marcaService.eliminarMarca(seleccionada.getId());

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        cargarTabla();
        limpiarFormulario();
    }

    @FXML
    private void handleAgregar() {
        MarcaDto marcaNueva = leerFormulario(null);

        if (marcaNueva == null) {
            return;
        }

        Respuesta respuesta = marcaService.guardarMarca(marcaNueva);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

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

        Respuesta respuesta = marcaService.guardarMarca(marcaModificada);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

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

        //Invertimos el tipo de la marca, si era entrada pasa a salida y viceversa
        if (seleccionada.getTipo().equals("ENTRADA")) {
            seleccionada.setTipo("SALIDA");
        } else {
            seleccionada.setTipo("ENTRADA");
        }

        seleccionada.setEstado("OK");
        marcaService.guardarMarca(seleccionada);

        handleVerInconsistencias();
    }

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

        String folio = folioTexto.trim();

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

    private void cargarFormulario(MarcaDto marca) {
        txtFolioMarca.setText(marca.getFolioEmpleado());
        dpFechaMarca.setValue(marca.getFechaHora().toLocalDate());
        txtHoraMarca.setText(marca.getFechaHora().toLocalTime().format(formatoHora));
        cmbTipoMarca.setValue(marca.getTipo());
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

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }
}
