/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cr.ac.una.relojuna.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Ortiz
 */
public class MantenimientoMarcasController {

    @FXML
    private DatePicker dpFechaDesde;
    @FXML
    private DatePicker dpFechaHasta;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnVerInconsistencias;
    @FXML
    private Label lblCantInconsistencias;
    @FXML
    private Button btnSiguienteInconsistencia;
    @FXML
    private TableView<?> tblMarcas;
    @FXML
    private TableColumn<?, ?> colEmpleado;
    @FXML
    private TableColumn<?, ?> colFechaHora;
    @FXML
    private TableColumn<?, ?> colTipo;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCorregirInconsistencia;

    @FXML
    private void handleFiltrar(ActionEvent event) {
    }

    @FXML
    private void handleVerInconsistencias(ActionEvent event) {
    }

    @FXML
    private void handleSiguienteInconsistencia(ActionEvent event) {
    }

    @FXML
    private void handleAgregar(ActionEvent event) {
    }

    @FXML
    private void handleModificar(ActionEvent event) {
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
    }

    @FXML
    private void handleCorregirInconsistencia(ActionEvent event) {
    }

}
