package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.service.IEmpleadoService;
import cr.ac.una.relojuna.service.IMarcaService;
import cr.ac.una.relojuna.service.ServiceFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MarcaController {

    @FXML
    private Label lblReloj, lblNombreEmpleado, lblHoraMarca, lblMensaje;
    @FXML
    private TextField txtFolio;
    @FXML
    private ImageView imgFoto;
    @FXML
    private Button btnMarcar;
    @FXML
    private Button btnRegresar;

    // Servicios que se usan en esta pantalla
    private IEmpleadoService empleadoService;
    private IMarcaService marcaService;

    // Formato para mostrar la hora en el reloj digital
    private DateTimeFormatter formatoReloj = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private void initialize() {
        empleadoService = ServiceFactory.getEmpleadoService();
        marcaService = ServiceFactory.getMarcaService();

        iniciarReloj();
    }

    // Arranca un timeline que actualiza el label del reloj cada segundo
    private void iniciarReloj() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), evento -> actualizarReloj())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Ponemos la hora inmediatamente para no esperar el primer segundo
        actualizarReloj();
    }

    private void actualizarReloj() {
        LocalDateTime ahora = LocalDateTime.now();
        lblReloj.setText(ahora.format(formatoReloj));
    }

    @FXML
    private void handleMarcar() {
        String folioTexto = txtFolio.getText();

        if (folioTexto == null || folioTexto.isBlank()) {
            lblMensaje.setText("Debe ingresar el folio.");
            return;
        }

        Integer folio;
        try {
            folio = Integer.valueOf(folioTexto);
        } catch (NumberFormatException ex) {
            lblMensaje.setText("El folio debe ser un numero.");
            return;
        }

        // Buscamos el empleado para mostrar su nombre y validar que exista
        List<EmpleadoDto> empleados = empleadoService.buscarEmpleados(folioTexto);

        if (empleados.isEmpty()) {
            lblMensaje.setText("No existe un empleado con ese folio.");
            limpiarInformacion();
            return;
        }

        EmpleadoDto empleado = empleados.get(0);

        // Registramos la marca
        MarcaDto marca = marcaService.marcar(folio);

        lblNombreEmpleado.setText(empleado.getNombre() + " " + empleado.getApellidos());
        lblHoraMarca.setText(marca.getTipo() + " registrada a las " + marca.getFechaHora().format(formatoReloj));
        lblMensaje.setText("Marca registrada correctamente.");

        // Verificamos si hoy es el cumpleanos del empleado
        if (esCumpleanios(empleado)) {
            mostrarAnimacionCumpleanios();
        }

        txtFolio.clear();
    }

    // Compara el dia y mes de nacimiento con la fecha de hoy
    private boolean esCumpleanios(EmpleadoDto empleado) {
        LocalDate fechaNacimiento = empleado.getFechaNacimiento();
        LocalDate hoy = LocalDate.now();

        if (fechaNacimiento == null) {
            return false;
        }

        boolean mismoMes = fechaNacimiento.getMonthValue() == hoy.getMonthValue();
        boolean mismoDia = fechaNacimiento.getDayOfMonth() == hoy.getDayOfMonth();

        return mismoMes && mismoDia;
    }

    // Animacion simple, la foto crece y vuelve a su tamano varias veces
    private void mostrarAnimacionCumpleanios() {
        lblMensaje.setText("Feliz cumpleanios " + lblNombreEmpleado.getText() + "!");

        ScaleTransition animacion = new ScaleTransition(Duration.millis(400), imgFoto);
        animacion.setFromX(1.0);
        animacion.setFromY(1.0);
        animacion.setToX(1.3);
        animacion.setToY(1.3);
        animacion.setCycleCount(4);
        animacion.setAutoReverse(true);
        animacion.play();
    }

    private void limpiarInformacion() {
        lblNombreEmpleado.setText("");
        lblHoraMarca.setText("");
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }
}