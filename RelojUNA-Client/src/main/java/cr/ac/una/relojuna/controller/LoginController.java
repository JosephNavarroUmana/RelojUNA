package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.ILoginService;
import cr.ac.una.relojuna.service.ServiceFactory;
import cr.ac.una.relojuna.util.FlowController;
import cr.ac.una.relojuna.util.SesionTemporal;
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

        //Validamos que los campos no vengan vacios
        if (folio == null || folio.isBlank() || clave == null || clave.isBlank()) {
            lblMensaje.setText("Debe ingresar el folio y la clave.");
            return;
        }

        ILoginService loginService = ServiceFactory.getLoginService();
        EmpleadoDto empleado = loginService.validarLogin(folio, clave);

        if (empleado == null) {
            lblMensaje.setText("Folio o clave incorrectos.");
            return;
        }

        //Guardamos el empleado en la sesion para usarlo en las demas pantallas
        SesionTemporal.getInstancia().setEmpleadoActual(empleado);

        //Cambiamos a la pantalla principal
        FlowController.getInstancia().irAVista("PrincipalView.fxml", "Menu Principal");
    }
}