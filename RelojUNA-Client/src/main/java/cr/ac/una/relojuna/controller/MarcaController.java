package cr.ac.una.relojuna.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MarcaController {

    @FXML private Label lblReloj;
    @FXML private Label lblMensaje;
    @FXML
    private TextField txtFolio;
    @FXML
    private Button btnMarcar;
    @FXML
    private ImageView imgFoto;
    @FXML
    private Label lblNombreEmpleado;
    @FXML
    private Label lblHoraMarca;

    public void initialize() {
        // Iniciar hilo para el reloj digital
    }

    @FXML
    private void handleMarcar(ActionEvent event) {
        // Lógica de marca (entrada/salida)
    }
}
