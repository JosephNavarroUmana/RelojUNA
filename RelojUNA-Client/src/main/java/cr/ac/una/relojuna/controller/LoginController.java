package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.LoginService;
import cr.ac.una.relojuna.util.AppContext;
import cr.ac.una.relojuna.util.FlowController;
import cr.ac.una.relojuna.util.Respuesta;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtFolio;
    @FXML
    private PasswordField txtClave;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblMensaje;

    @FXML
    private void handleLogin() {
        String folio = txtFolio.getText();
        String clave = txtClave.getText();

        if (folio == null || folio.isBlank()) {
            lblMensaje.setText("Debe ingresar el folio.");
            return;
        }

        if (clave == null || clave.isBlank()) {
            lblMensaje.setText("Debe ingresar la clave.");
            return;
        }

        LoginService loginService = new LoginService();
        Respuesta respuesta = loginService.validarLogin(folio, clave);

        if (!respuesta.getEstado()) {
            lblMensaje.setText(respuesta.getMensaje());
            //Limpiamos la clave para que no quede pegada en el campo tras un intento fallido
            txtClave.clear();
            return;
        }

        EmpleadoDto empleado = (EmpleadoDto) respuesta.getResultado("Usuario");

        AppContext.getInstance().set("Usuario", empleado);

        FlowController.getInstancia().irAVista("PrincipalView.fxml", "Menu Principal");
    }
}