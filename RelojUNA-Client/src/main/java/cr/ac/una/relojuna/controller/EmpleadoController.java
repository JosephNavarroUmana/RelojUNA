/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cr.ac.una.relojuna.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * @author Ortiz
 */
public class EmpleadoController {

    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiarBusqueda;
    @FXML
    private TableView<?> tblEmpleados;
    @FXML
    private TableColumn<?, ?> colFolio;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colApellidos;
    @FXML
    private TableColumn<?, ?> colCedula;
    @FXML
    private TableColumn<?, ?> colFechaNac;
    @FXML
    private TableColumn<?, ?> colSalario;
    @FXML
    private TableColumn<?, ?> colAdmin;
    @FXML
    private TextField txtFolio;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtCedula;
    @FXML
    private DatePicker dpFechaNac;
    @FXML
    private TextField txtSalario;
    @FXML
    private TextField txtFoto;
    @FXML
    private PasswordField txtClave;
    @FXML
    private CheckBox chkAdmin;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;

    @FXML
    private void handleBuscar(ActionEvent event) {
    }

    @FXML
    private void handleLimpiarBusqueda(ActionEvent event) {
    }

    @FXML
    private void handleNuevo(ActionEvent event) {
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
    }

    @FXML
    private void handleLimpiar(ActionEvent event) {
    }

}
