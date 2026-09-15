package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.EmpleadoService;
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

        //Validamos que los campos no vengan vacios
        if (folio == null || folio.isBlank() || clave == null || clave.isBlank()) {
            lblMensaje.setText("Debe ingresar el folio y la clave.");
            return;
        }

        EmpleadoService empleadoService = new EmpleadoService();
        Respuesta respuesta = empleadoService.validarLogin(folio, clave);

        if (!respuesta.getEstado()) {
            lblMensaje.setText(respuesta.getMensaje());
            return;
        }

        EmpleadoDto empleado = (EmpleadoDto) respuesta.getResultado("Usuario");

        //Guardamos el empleado en el contexto para usarlo en las demas pantallas
        AppContext.getInstance().set("Usuario", empleado);

        //Cambiamos a la pantalla principal
        FlowController.getInstancia().irAVista("PrincipalView.fxml", "Menu Principal");
    }
}