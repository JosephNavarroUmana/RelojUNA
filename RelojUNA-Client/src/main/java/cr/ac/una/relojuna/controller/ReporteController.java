/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cr.ac.una.relojuna.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * @author Ortiz
 */
public class ReporteController {

    @FXML
    private RadioButton rbReporteEmpleados;
    @FXML
    private RadioButton rbReporteMarcas;
    @FXML
    private ToggleGroup tipoReporteGroup;
    @FXML
    private ComboBox<?> cmbEmpleado;
    @FXML
    private DatePicker dpFechaDesde;
    @FXML
    private DatePicker dpFechaHasta;
    @FXML
    private Button btnGenerarReporte;
    @FXML
    private Button btnVistaPrevia;

    @FXML
    private void handleGenerarReporte(ActionEvent event) {
    }

    @FXML
    private void handleVistaPrevia(ActionEvent event) {
    }

}
