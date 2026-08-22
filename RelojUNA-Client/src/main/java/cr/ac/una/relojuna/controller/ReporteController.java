package cr.ac.una.relojuna.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ReporteController {

    @FXML
    private RadioButton rbReporteEmpleados, rbReporteMarcas;
    @FXML
    private ComboBox<?> cmbEmpleado;
    @FXML
    private DatePicker dpFechaDesde, dpFechaHasta;
    @FXML
    private Button btnGenerarReporte, btnVistaPrevia;
    @FXML
    private Button btnRegresar;

    @FXML
    private void handleGenerarReporte() { /* ... */ }

    @FXML
    private void handleVistaPrevia() { /* ... */ }

    @FXML
    private void handleRegresar(ActionEvent event) {
        
    }
}