/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cr.ac.una.relojuna.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * @author Ortiz
 */
public class PlanillaController {

    @FXML
    private TextField txtAnio;
    @FXML
    private ComboBox<?> cmbMes;
    @FXML
    private Button btnGenerar;
    @FXML
    private Button btnExportarExcel;
    @FXML
    private TableView<?> tblPlanilla;
    @FXML
    private TableColumn<?, ?> colEmpleado;
    @FXML
    private TableColumn<?, ?> colHorasOrdinarias;
    @FXML
    private TableColumn<?, ?> colHorasExtras;
    @FXML
    private TableColumn<?, ?> colHorasDobles;
    @FXML
    private TableColumn<?, ?> colSalarioMensual;

    @FXML
    private void handleGenerar(ActionEvent event) {
    }

    @FXML
    private void handleExportarExcel(ActionEvent event) {
    }

}
