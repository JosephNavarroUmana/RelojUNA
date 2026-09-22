package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.ConsultaService;
import cr.ac.una.relojuna.service.EmpleadoService;
import cr.ac.una.relojuna.service.ReporteService;
import cr.ac.una.relojuna.util.Respuesta;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ReporteController {

    @FXML
    private RadioButton rbReporteEmpleados, rbReporteMarcas;
    @FXML
    private ComboBox<String> cmbEmpleado;
    @FXML
    private DatePicker dpFechaDesde, dpFechaHasta;
    @FXML
    private Button btnGenerarReporte;
    @FXML
    private Button btnRegresar;

    private EmpleadoService empleadoService;
    private ConsultaService consultaService;
    private ReporteService reporteService;

    @FXML
    private void initialize() {
        empleadoService = new EmpleadoService();
        consultaService = new ConsultaService();
        reporteService = new ReporteService();

        cargarComboEmpleados();

        dpFechaDesde.setValue(LocalDate.now().minusDays(30));
        dpFechaHasta.setValue(LocalDate.now());

        ToggleGroup grupoTipoReporte = new ToggleGroup();
        rbReporteEmpleados.setToggleGroup(grupoTipoReporte);
        rbReporteMarcas.setToggleGroup(grupoTipoReporte);
        rbReporteMarcas.setSelected(true);

        grupoTipoReporte.selectedToggleProperty().addListener((observable, anterior, seleccionado) -> {
            actualizarControlesSegunTipo();
        });

        actualizarControlesSegunTipo();
    }

    private void cargarComboEmpleados() {
        Respuesta respuesta = empleadoService.buscarEmpleados("");

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        List<EmpleadoDto> empleados = (List<EmpleadoDto>) respuesta.getResultado("Empleados");

        ObservableList<String> opciones = FXCollections.observableArrayList();
        opciones.add("Todos");

        for (EmpleadoDto empleado : empleados) {
            opciones.add(empleado.getFolio() + " - " + empleado.getNombre() + " " + empleado.getApellidos());
        }

        cmbEmpleado.setItems(opciones);
        cmbEmpleado.setValue("Todos");
    }

    private void actualizarControlesSegunTipo() {
        boolean esReporteMarcas = rbReporteMarcas.isSelected();

        cmbEmpleado.setDisable(!esReporteMarcas);
        dpFechaDesde.setDisable(!esReporteMarcas);
        dpFechaHasta.setDisable(!esReporteMarcas);
    }

    private String obtenerFolioSeleccionado() {
        String seleccionado = cmbEmpleado.getValue();
        if (seleccionado == null || seleccionado.equals("Todos")) {
            return null;
        }
        return seleccionado.split(" - ")[0];
    }

    private List<ConsultaResultadoDto> obtenerMarcasOrdenadas() {
        LocalDate fechaDesde = dpFechaDesde.getValue();
        LocalDate fechaHasta = dpFechaHasta.getValue();
        String folioEmpleado = obtenerFolioSeleccionado();

        Respuesta respuesta = consultaService.consultarMarcas(fechaDesde, fechaHasta, folioEmpleado);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return List.of();
        }

        List<ConsultaResultadoDto> marcas = (List<ConsultaResultadoDto>) respuesta.getResultado("Consultas");
        marcas.sort((marca1, marca2) -> marca1.getNombreEmpleado().compareTo(marca2.getNombreEmpleado()));

        return marcas;
    }

    private List<EmpleadoDto> obtenerTodosLosEmpleados() {
        Respuesta respuesta = empleadoService.buscarEmpleados("");

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return List.of();
        }

        return (List<EmpleadoDto>) respuesta.getResultado("Empleados");
    }

    @FXML
    private void handleVistaPrevia() {
        try {
            Respuesta respuesta;

            if (rbReporteEmpleados.isSelected()) {
                List<EmpleadoDto> empleados = obtenerTodosLosEmpleados();
                respuesta = reporteService.generarReporteEmpleadosPdf(empleados);
            } else {
                List<ConsultaResultadoDto> marcas = obtenerMarcasOrdenadas();
                respuesta = reporteService.generarReporteMarcasPdf(marcas);
            }

            if (!respuesta.getEstado()) {
                mostrarMensaje(respuesta.getMensaje());
                return;
            }

            byte[] bytesPdf = (byte[]) respuesta.getResultado("Pdf");

            File archivoTemporal = File.createTempFile("VistaPrevia", ".pdf");
            archivoTemporal.deleteOnExit();

            try (FileOutputStream salida = new FileOutputStream(archivoTemporal)) {
                salida.write(bytesPdf);
            }

            Desktop.getDesktop().open(archivoTemporal);
        } catch (Exception ex) {
            mostrarMensaje("Error generando la vista previa: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleGenerarReporte() {
        if (rbReporteEmpleados.isSelected()) {
            generarReporteEmpleados();
        } else {
            generarReporteMarcas();
        }
    }

    private void generarReporteEmpleados() {
        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar reporte de empleados");
        selector.setInitialFileName("ReporteEmpleados.pdf");
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File archivo = selector.showSaveDialog(btnGenerarReporte.getScene().getWindow());

        if (archivo == null) {
            return;
        }

        List<EmpleadoDto> empleados = obtenerTodosLosEmpleados();
        Respuesta respuesta = reporteService.generarReporteEmpleadosPdf(empleados);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        byte[] bytesPdf = (byte[]) respuesta.getResultado("Pdf");
        guardarBytesEnArchivo(bytesPdf, archivo);
    }

    private void generarReporteMarcas() {
        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar reporte de marcas");
        selector.setInitialFileName("ReporteMarcas.pdf");
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File archivo = selector.showSaveDialog(btnGenerarReporte.getScene().getWindow());

        if (archivo == null) {
            return;
        }

        List<ConsultaResultadoDto> marcas = obtenerMarcasOrdenadas();
        Respuesta respuesta = reporteService.generarReporteMarcasPdf(marcas);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        byte[] bytesPdf = (byte[]) respuesta.getResultado("Pdf");
        guardarBytesEnArchivo(bytesPdf, archivo);
    }

    private void guardarBytesEnArchivo(byte[] bytes, File archivo) {
        try (FileOutputStream salida = new FileOutputStream(archivo)) {
            salida.write(bytes);
            mostrarMensaje("Reporte generado correctamente.");
        } catch (Exception ex) {
            mostrarMensaje("Error generando el reporte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
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
