package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.service.EmpleadoService;
import cr.ac.una.relojuna.service.MarcaService;
import cr.ac.una.relojuna.util.AnimacionCumpleanos;
import cr.ac.una.relojuna.util.Respuesta;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MarcaController {

    @FXML
    private Label lblReloj, lblFecha, lblNombreEmpleado, lblHoraMarca, lblMensaje, lblFelicitacion;
    @FXML
    private TextField txtFolio;
    @FXML
    private ImageView imgFoto;
    @FXML
    private Button btnMarcar;
    @FXML
    private Button btnRegresar;
    @FXML
    private VBox panelResultado;
    @FXML
    private StackPane panelAnimacion;

    //Servicios que se usan en esta pantalla
//    private EmpleadoService empleadoService;
//    private MarcaService marcaService;
//
//    //Formatos para mostrar la hora y la fecha en el reloj digital
//    private DateTimeFormatter formatoReloj = DateTimeFormatter.ofPattern("HH:mm:ss");
//    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "CR"));

        private EmpleadoService empleadoService = new EmpleadoService();
    private MarcaService marcaService = new MarcaService();

    //Formatos para mostrar la hora y la fecha en el reloj digital
    private DateTimeFormatter formatoReloj = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "CR"));
    
  @FXML
private void initialize() {
    empleadoService = new EmpleadoService();
    marcaService = new MarcaService();

    //El circulo de recorte deja la foto redonda dentro del marco del FXML
    Circle recorteFoto = new Circle(45, 45, 45);
    imgFoto.setClip(recorteFoto);

    iniciarReloj();
}
    //Arranca un timeline que actualiza el label del reloj cada segundo
    private void iniciarReloj() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), evento -> actualizarReloj())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Ponemos la hora inmediatamente para no esperar el primer segundo
        actualizarReloj();
    }

    private void actualizarReloj() {
        LocalDateTime ahora = LocalDateTime.now();
        lblReloj.setText(ahora.format(formatoReloj));
        lblFecha.setText(ahora.format(formatoFecha));
    }

    @FXML
    private void handleMarcar() {
        String folioTexto = txtFolio.getText();

        if (folioTexto == null || folioTexto.isBlank()) {
            lblMensaje.setText("Debe ingresar el folio.");
            return;
        }

        //Buscamos el empleado para mostrar su nombre y validar que exista
        Respuesta respuestaEmpleados = empleadoService.buscarEmpleados(folioTexto);

        if (!respuestaEmpleados.getEstado()) {
            lblMensaje.setText(respuestaEmpleados.getMensaje());
            return;
        }

        List<EmpleadoDto> empleados = (List<EmpleadoDto>) respuestaEmpleados.getResultado("Empleados");

        if (empleados.isEmpty()) {
            lblMensaje.setText("No existe un empleado con ese folio.");
            limpiarInformacion();
            return;
        }

        EmpleadoDto empleado = empleados.get(0);

        //Registramos la marca usando el id real del empleado, no el folio escrito
        Respuesta respuestaMarca = marcaService.marcar(empleado.getId());

        if (!respuestaMarca.getEstado()) {
            lblMensaje.setText(respuestaMarca.getMensaje());
            return;
        }

        MarcaDto marca = (MarcaDto) respuestaMarca.getResultado("Marca");

        mostrarFoto(empleado);
        lblNombreEmpleado.setText(empleado.getNombre() + " " + empleado.getApellidos());
        lblHoraMarca.setText(marca.getTipo() + " registrada a las " + marca.getFechaHora().format(formatoReloj));
        lblMensaje.setText("Marca registrada correctamente.");

        //Verificamos si hoy es el cumpleanios del empleado
        if (esCumpleanios(empleado)) {
            mostrarAnimacionCumpleanios(empleado);
        }

        txtFolio.clear();
    }

    //Convierte el arreglo de bytes de la BD en una imagen para el ImageView
    private void mostrarFoto(EmpleadoDto empleado) {
        byte[] datosFoto = empleado.getFoto();

        if (datosFoto == null || datosFoto.length == 0) {
            imgFoto.setImage(null);
            return;
        }

        Image imagen = new Image(new ByteArrayInputStream(datosFoto));
        imgFoto.setImage(imagen);
    }

    //Compara el dia y mes de nacimiento con la fecha de hoy
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

   private void mostrarAnimacionCumpleanios(EmpleadoDto empleado) {
        AnimacionCumpleanos.reproducir(panelAnimacion, lblFelicitacion, empleado.getNombre());
    }

    private void limpiarInformacion() {
        lblNombreEmpleado.setText("");
        lblHoraMarca.setText("");
        imgFoto.setImage(null);
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }
}