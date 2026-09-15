package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.ConsultaService;
import cr.ac.una.relojuna.service.EmpleadoService;
import cr.ac.una.relojuna.util.JasperExportador;
import cr.ac.una.relojuna.util.Respuesta;
import java.io.File;
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
    private Button btnGenerarReporte, btnVistaPrevia;
    @FXML
    private Button btnRegresar;

    //Servicios usados en esta pantalla
    private EmpleadoService empleadoService;
    private ConsultaService consultaService;

    @FXML
    private void initialize() {
        empleadoService = new EmpleadoService();
        consultaService = new ConsultaService();

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

    //Llena el combo de empleados con la opcion Todos de primero
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

    private Integer obtenerFolioSeleccionado() {
        String seleccionado = cmbEmpleado.getValue();

        if (seleccionado == null || seleccionado.equals("Todos")) {
            return null;
        }

        String folioTexto = seleccionado.split(" - ")[0];
        return Integer.valueOf(folioTexto);
    }

    //Trae las marcas segun los filtros de la pantalla y las ordena por empleado para que el reporte agrupe bien
    private List<ConsultaResultadoDto> obtenerMarcasOrdenadas() {
        LocalDate fechaDesde = dpFechaDesde.getValue();
        LocalDate fechaHasta = dpFechaHasta.getValue();
        Integer folioEmpleado = obtenerFolioSeleccionado();

        Respuesta respuesta = consultaService.consultarMarcas(fechaDesde, fechaHasta, folioEmpleado);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return List.of();
        }

        List<ConsultaResultadoDto> marcas = (List<ConsultaResultadoDto>) respuesta.getResultado("Consultas");
        marcas.sort((marca1, marca2) -> marca1.getNombreEmpleado().compareTo(marca2.getNombreEmpleado()));

        return marcas;
    }

    //Trae todos los empleados para el reporte de empleados
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
            if (rbReporteEmpleados.isSelected()) {
                List<EmpleadoDto> empleados = obtenerTodosLosEmpleados();
                JasperExportador.mostrarVistaPreviaEmpleados(empleados);
            } else {
                List<ConsultaResultadoDto> marcas = obtenerMarcasOrdenadas();
                JasperExportador.mostrarVistaPreviaMarcas(marcas);
            }
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

    //Genera el PDF del reporte de empleados y lo guarda donde el usuario elija
    private void generarReporteEmpleados() {
        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar reporte de empleados");
        selector.setInitialFileName("ReporteEmpleados.pdf");
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File archivo = selector.showSaveDialog(btnGenerarReporte.getScene().getWindow());

        if (archivo == null) {
            return;
        }

        try {
            List<EmpleadoDto> empleados = obtenerTodosLosEmpleados();
            JasperExportador.exportarEmpleadosAPdf(empleados, archivo);
            mostrarMensaje("Reporte generado correctamente.");
        } catch (Exception ex) {
            mostrarMensaje("Error generando el reporte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    //Genera el PDF del reporte de marcas y lo guarda donde el usuario elija
    private void generarReporteMarcas() {
        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar reporte de marcas");
        selector.setInitialFileName("ReporteMarcas.pdf");
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File archivo = selector.showSaveDialog(btnGenerarReporte.getScene().getWindow());

        if (archivo == null) {
            return;
        }

        try {
            List<ConsultaResultadoDto> marcas = obtenerMarcasOrdenadas();
            JasperExportador.exportarMarcasAPdf(marcas, archivo);
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