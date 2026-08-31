package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.IConsultaService;
import cr.ac.una.relojuna.service.IEmpleadoService;
import cr.ac.una.relojuna.service.ServiceFactory;
import cr.ac.una.relojuna.util.JasperExportador;
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

    // Servicios usados en esta pantalla
    private IEmpleadoService empleadoService;
    private IConsultaService consultaService;

    @FXML
    private void initialize() {
        empleadoService = ServiceFactory.getEmpleadoService();
        consultaService = ServiceFactory.getConsultaService();

        cargarComboEmpleados();

        dpFechaDesde.setValue(LocalDate.now().minusDays(30));
        dpFechaHasta.setValue(LocalDate.now());

        // Creamos el grupo de radios a mano, para que ambos radios se comporten como mutuamente excluyentes
        ToggleGroup grupoTipoReporte = new ToggleGroup();
        rbReporteEmpleados.setToggleGroup(grupoTipoReporte);
        rbReporteMarcas.setToggleGroup(grupoTipoReporte);
        rbReporteMarcas.setSelected(true);

        // Cuando cambia el tipo de reporte, habilitamos o deshabilitamos los controles que le corresponden
        grupoTipoReporte.selectedToggleProperty().addListener((observable, anterior, seleccionado) -> {
            actualizarControlesSegunTipo();
        });

        actualizarControlesSegunTipo();
    }

    // Llena el combo de empleados con la opcion Todos de primero
    private void cargarComboEmpleados() {
        List<EmpleadoDto> empleados = empleadoService.buscarEmpleados("");

        ObservableList<String> opciones = FXCollections.observableArrayList();
        opciones.add("Todos");

        for (EmpleadoDto empleado : empleados) {
            opciones.add(empleado.getFolio() + " - " + empleado.getNombre() + " " + empleado.getApellidos());
        }

        cmbEmpleado.setItems(opciones);
        cmbEmpleado.setValue("Todos");
    }

    // El reporte de empleados no usa fechas ni empleado especifico, el de marcas si
    private void actualizarControlesSegunTipo() {
        boolean esReporteMarcas = rbReporteMarcas.isSelected();

        cmbEmpleado.setDisable(!esReporteMarcas);
        dpFechaDesde.setDisable(!esReporteMarcas);
        dpFechaHasta.setDisable(!esReporteMarcas);
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

    // Trae las marcas segun los filtros de la pantalla y las ordena por empleado para que el reporte agrupe bien
    private List<ConsultaResultadoDto> obtenerMarcasOrdenadas() {
        LocalDate fechaDesde = dpFechaDesde.getValue();
        LocalDate fechaHasta = dpFechaHasta.getValue();
        Integer folioEmpleado = obtenerFolioSeleccionado();

        List<ConsultaResultadoDto> marcas = consultaService.consultarMarcas(fechaDesde, fechaHasta, folioEmpleado);

        marcas.sort((marca1, marca2) -> marca1.getNombreEmpleado().compareTo(marca2.getNombreEmpleado()));

        return marcas;
    }

    @FXML
    private void handleVistaPrevia() {
        try {
            if (rbReporteEmpleados.isSelected()) {
                List<EmpleadoDto> empleados = empleadoService.buscarEmpleados("");
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

    // Genera el PDF del reporte de empleados y lo guarda donde el usuario elija
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
            List<EmpleadoDto> empleados = empleadoService.buscarEmpleados("");
            JasperExportador.exportarEmpleadosAPdf(empleados, archivo);
            mostrarMensaje("Reporte generado correctamente.");
        } catch (Exception ex) {
            mostrarMensaje("Error generando el reporte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Genera el PDF del reporte de marcas y lo guarda donde el usuario elija
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