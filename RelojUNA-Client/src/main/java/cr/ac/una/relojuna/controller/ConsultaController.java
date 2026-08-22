package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.service.IConsultaService;
import cr.ac.una.relojuna.service.IEmpleadoService;
import cr.ac.una.relojuna.service.IMarcaService;
import cr.ac.una.relojuna.service.ServiceFactory;
import cr.ac.una.relojuna.util.ExcelExportador;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ConsultaController {

    @FXML
    private DatePicker dpFechaDesde, dpFechaHasta;
    @FXML
    private ComboBox<String> cmbEmpleado;
    @FXML
    private Label lblTotalEmpleados, lblTotalMarcas, lblTotalHoras;
    @FXML
    private TableView<ConsultaResultadoDto> tblResultados;
    @FXML
    private TableColumn<ConsultaResultadoDto, String> colEmpleado, colFecha, colHoraEntrada, colHoraSalida;
    @FXML
    private TableColumn<ConsultaResultadoDto, Double> colHorasTrabajadas;
    @FXML
    private Button btnConsultar, btnExportarExcel;
    @FXML
    private Button btnRegresar;

    // Servicios usados en esta pantalla
    private IConsultaService consultaService;
    private IEmpleadoService empleadoService;
    private IMarcaService marcaService;

    // Lista observable que alimenta la tabla
    private ObservableList<ConsultaResultadoDto> listaResultados;

    // Lista de empleados cargados en el combo
    private List<EmpleadoDto> empleadosDelCombo;

    private DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private void initialize() {
        consultaService = ServiceFactory.getConsultaService();
        empleadoService = ServiceFactory.getEmpleadoService();
        marcaService = ServiceFactory.getMarcaService();
        listaResultados = FXCollections.observableArrayList();

        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        colHorasTrabajadas.setCellValueFactory(new PropertyValueFactory<>("horasTrabajadas"));

        colFecha.setCellValueFactory(datos -> {
            String texto = datos.getValue().getFecha().toString();
            return new javafx.beans.property.SimpleStringProperty(texto);
        });
        colHoraEntrada.setCellValueFactory(datos -> {
            String texto = datos.getValue().getHoraEntrada().format(formatoHora);
            return new javafx.beans.property.SimpleStringProperty(texto);
        });
        colHoraSalida.setCellValueFactory(datos -> {
            String texto = datos.getValue().getHoraSalida().format(formatoHora);
            return new javafx.beans.property.SimpleStringProperty(texto);
        });

        tblResultados.setItems(listaResultados);

        cargarComboEmpleados();

        // Por defecto mostramos los ultimos 30 dias
        dpFechaDesde.setValue(LocalDate.now().minusDays(30));
        dpFechaHasta.setValue(LocalDate.now());
    }

    // Llena el combo de empleados con la opcion Todos de primero
    private void cargarComboEmpleados() {
        empleadosDelCombo = empleadoService.buscarEmpleados("");

        ObservableList<String> opciones = FXCollections.observableArrayList();
        opciones.add("Todos");

        for (EmpleadoDto empleado : empleadosDelCombo) {
            opciones.add(empleado.getFolio() + " - " + empleado.getNombre() + " " + empleado.getApellidos());
        }

        cmbEmpleado.setItems(opciones);
        cmbEmpleado.setValue("Todos");
    }

    // Obtiene el folio del empleado seleccionado en el combo, null si esta en Todos
    private Integer obtenerFolioSeleccionado() {
        String seleccionado = cmbEmpleado.getValue();

        if (seleccionado == null || seleccionado.equals("Todos")) {
            return null;
        }

        String folioTexto = seleccionado.split(" - ")[0];
        return Integer.valueOf(folioTexto);
    }

    @FXML
    private void handleConsultar() {
        LocalDate fechaDesde = dpFechaDesde.getValue();
        LocalDate fechaHasta = dpFechaHasta.getValue();

        if (fechaDesde == null || fechaHasta == null) {
            mostrarMensaje("Debe seleccionar la fecha desde y la fecha hasta.");
            return;
        }

        Integer folioEmpleado = obtenerFolioSeleccionado();

        List<ConsultaResultadoDto> resultado = consultaService.consultarMarcas(fechaDesde, fechaHasta, folioEmpleado);
        listaResultados.clear();
        listaResultados.addAll(resultado);

        actualizarTotales(fechaDesde, fechaHasta, folioEmpleado, resultado);
    }

    // Calcula los totales de la consulta usando streams
    private void actualizarTotales(LocalDate fechaDesde, LocalDate fechaHasta, Integer folioEmpleado, List<ConsultaResultadoDto> resultado) {
        long totalEmpleados = resultado.stream()
                .map(fila -> fila.getFolioEmpleado())
                .distinct()
                .count();

        double totalHoras = resultado.stream()
                .mapToDouble(fila -> fila.getHorasTrabajadas())
                .sum();

        List<MarcaDto> marcasDelRango = marcaService.buscarMarcas(fechaDesde, fechaHasta);

        long totalMarcas = marcasDelRango.stream()
                .filter(marca -> folioEmpleado == null || marca.getFolioEmpleado().equals(folioEmpleado))
                .count();

        lblTotalEmpleados.setText("Total empleados: " + totalEmpleados);
        lblTotalMarcas.setText("Total marcas: " + totalMarcas);
        lblTotalHoras.setText("Total horas trabajadas: " + String.format("%.2f", totalHoras));
    }

    @FXML
private void handleExportarExcel() {
    if (listaResultados.isEmpty()) {
        mostrarMensaje("Debe realizar una consulta antes de exportar.");
        return;
    }

    FileChooser selector = new FileChooser();
    selector.setTitle("Guardar consulta como Excel");
    selector.setInitialFileName("ConsultaMarcas.xlsx");
    selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx"));

    File archivo = selector.showSaveDialog(btnExportarExcel.getScene().getWindow());

    if (archivo == null) {
        return;
    }

    try {
        ExcelExportador.exportarConsultas(listaResultados, archivo);
        mostrarMensaje("Archivo exportado correctamente.");
    } catch (IOException ex) {
        mostrarMensaje("Ocurrio un error al exportar el archivo.");
    }
}

    @FXML
    private void handleRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}