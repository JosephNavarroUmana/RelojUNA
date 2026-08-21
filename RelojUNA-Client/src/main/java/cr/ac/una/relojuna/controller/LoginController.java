package cr.ac.una.relojuna.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtFolio;
    @FXML private PasswordField txtClave;
    @FXML
    private Button btnLogin;

    @FXML
    private void handleLogin(ActionEvent event) {
        // Lógica de login llamando al Web Service
    }
}
