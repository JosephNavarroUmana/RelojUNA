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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Ortiz
 */
public class ConsultaController {

    @FXML
    private DatePicker dpFechaDesde;
    @FXML
    private DatePicker dpFechaHasta;
    @FXML
    private ComboBox<?> cmbEmpleado;
    @FXML
    private Button btnConsultar;
    @FXML
    private Button btnExportarExcel;
    @FXML
    private Label lblTotalEmpleados;
    @FXML
    private Label lblTotalMarcas;
    @FXML
    private Label lblTotalHoras;
    @FXML
    private TableView<?> tblResultados;
    @FXML
    private TableColumn<?, ?> colEmpleado;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colHoraEntrada;
    @FXML
    private TableColumn<?, ?> colHoraSalida;
    @FXML
    private TableColumn<?, ?> colHorasTrabajadas;

    @FXML
    private void handleConsultar(ActionEvent event) {
    }

    @FXML
    private void handleExportarExcel(ActionEvent event) {
    }

}
