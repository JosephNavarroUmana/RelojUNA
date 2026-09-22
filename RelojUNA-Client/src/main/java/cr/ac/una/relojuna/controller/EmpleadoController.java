package cr.ac.una.relojuna.controller;

import com.github.sarxos.webcam.Webcam;
import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.service.EmpleadoService;
import cr.ac.una.relojuna.util.Respuesta;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

public class EmpleadoController {

    @FXML
    private TextField txtBuscar, txtFolio, txtNombre, txtApellidos, txtCedula, txtSalario;
    @FXML
    private DatePicker dpFechaNac;
    @FXML
    private PasswordField txtClave;
    @FXML
    private CheckBox chkAdmin;
    @FXML
    private ImageView imgFoto;
    @FXML
    private Button btnFoto;
    @FXML
    private TableView<EmpleadoDto> tblEmpleados;
    @FXML
    private TableColumn<EmpleadoDto, String> colFolio;
    @FXML
    private TableColumn<EmpleadoDto, String> colNombre, colApellidos, colCedula;
    @FXML
    private TableColumn<EmpleadoDto, LocalDate> colFechaNac;
    @FXML
    private TableColumn<EmpleadoDto, Double> colSalario;
    @FXML
    private TableColumn<EmpleadoDto, Boolean> colAdmin;
    @FXML
    private Button btnRegresar;

    private static final int EDAD_MINIMA = 18;

    private EmpleadoService empleadoService;
    private ObservableList<EmpleadoDto> listaEmpleados;
    private Long idSeleccionado;
    private byte[] fotoActual;
    private Webcam webcam;
    private Stage stageCamara;
    private Timeline timelineCamara;

    @FXML
    private void initialize() {
        empleadoService = new EmpleadoService();
        listaEmpleados = FXCollections.observableArrayList();

        colFolio.setCellValueFactory(new PropertyValueFactory<>("folio"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salarioPorHora"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<>("administrador"));

        tblEmpleados.setItems(listaEmpleados);

        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                cargarFormulario(seleccionado);
            }
        });

        txtClave.setDisable(!chkAdmin.isSelected());
        chkAdmin.selectedProperty().addListener((obs, anterior, esAdmin) -> {
            txtClave.setDisable(!esAdmin);
            if (!esAdmin) {
                txtClave.clear();
            }
        });

        dpFechaNac.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate fecha, boolean vacio) {
                super.updateItem(fecha, vacio);
                if (fecha == null) {
                    return;
                }
                boolean invalida = fecha.isAfter(LocalDate.now().minusYears(EDAD_MINIMA));
                setDisable(invalida);
                if (invalida) {
                    setStyle("-fx-background-color: #ffc0c0;");
                }
            }
        });

        txtNombre.textProperty().addListener((obs, textoAnterior, textoActual) -> {
            if (!textoActual.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ ]{0,15}")) {
                txtNombre.setText(textoAnterior);
            }
        });

        txtApellidos.textProperty().addListener((obs, textoAnterior, textoActual) -> {
            if (!textoActual.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ ]{0,20}")) {
                txtApellidos.setText(textoAnterior);
            }
        });
        txtCedula.textProperty().addListener((obs, textoAnterior, textoActual) -> {
            if (!textoActual.matches("[0-9]{0,9}")) {
                txtCedula.setText(textoAnterior);
            }
        });

        txtSalario.textProperty().addListener((obs, textoAnterior, textoActual) -> {
            if (textoActual.isBlank()) {
                return;
            }
            if (!textoActual.matches("[0-9]+")) {
                txtSalario.setText(textoAnterior);
                return;
            }

            int valorSalario;
            try {
                valorSalario = Integer.parseInt(textoActual);
            } catch (NumberFormatException ex) {
                txtSalario.setText(textoAnterior);
                return;
            }

            if (valorSalario > 100000) {
                txtSalario.setText(textoAnterior);
            }
        });

        txtClave.textProperty().addListener((obs, textoAnterior, textoActual) -> {
            if (!textoActual.matches("[a-zA-Z0-9]{0,10}")) {
                txtClave.setText(textoAnterior);
            }
        });

        btnRegresar.sceneProperty().addListener((obs, escenaAnterior, escenaNueva) -> {
            if (escenaNueva != null) {
                Stage stage = (Stage) escenaNueva.getWindow();
                stage.setOnCloseRequest(evento -> apagarCamara());
            }
        });

        cargarTabla("");
    }

    private void cargarTabla(String textoBusqueda) {
        Respuesta respuesta = empleadoService.buscarEmpleados(textoBusqueda);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        List<EmpleadoDto> empleados = (List<EmpleadoDto>) respuesta.getResultado("Empleados");
        listaEmpleados.clear();
        listaEmpleados.addAll(empleados);
    }

    private void cargarFormulario(EmpleadoDto empleado) {
        idSeleccionado = empleado.getId();

        txtFolio.setText(empleado.getFolio());
        txtNombre.setText(empleado.getNombre());
        txtApellidos.setText(empleado.getApellidos());
        txtCedula.setText(empleado.getCedula());
        dpFechaNac.setValue(empleado.getFechaNacimiento());
        txtSalario.setText(empleado.getSalarioPorHora().toString());
        chkAdmin.setSelected(empleado.isAdministrador());
        txtClave.setText(empleado.isAdministrador() ? empleado.getClave() : "");

        fotoActual = empleado.getFoto();
        mostrarPreviewFoto(fotoActual);
    }

    private void limpiarFormulario() {
        apagarCamara();

        idSeleccionado = null;
        fotoActual = null;

        txtFolio.clear();
        txtNombre.clear();
        txtApellidos.clear();
        txtCedula.clear();
        dpFechaNac.setValue(null);
        txtSalario.clear();
        txtClave.clear();
        chkAdmin.setSelected(false);
        mostrarPreviewFoto(null);
        tblEmpleados.getSelectionModel().clearSelection();
    }

    private void mostrarPreviewFoto(byte[] foto) {
        if (foto == null || foto.length == 0) {
            imgFoto.setImage(null);
            return;
        }
        imgFoto.setImage(new Image(new ByteArrayInputStream(foto)));
    }

    @FXML
    private void handleBuscar() {
        cargarTabla(txtBuscar.getText());
    }

    @FXML
    private void handleLimpiarBusqueda() {
        txtBuscar.clear();
        cargarTabla("");
    }

    @FXML
    private void handleSeleccionarFoto() {
        apagarCamara();

        FileChooser selector = new FileChooser();
        selector.setTitle("Seleccionar foto del empleado");
        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) btnFoto.getScene().getWindow();
        File archivo = selector.showOpenDialog(stage);

        if (archivo == null) {
            return;
        }

        try {
            byte[] bytesFoto = Files.readAllBytes(archivo.toPath());
            fotoActual = bytesFoto;
            mostrarPreviewFoto(bytesFoto);
        } catch (IOException ex) {
            mostrarMensaje("No se pudo leer el archivo de imagen seleccionado.");
        }
    }

    @FXML
    private void handleAbrirCamara() {
        try {
            webcam = Webcam.getDefault();

            if (webcam == null) {
                mostrarMensaje("No se encontro ninguna camara conectada.");
                return;
            }

            webcam.open();
        } catch (Exception ex) {
            mostrarMensaje("No se pudo abrir la camara.");
            webcam = null;
            return;
        }

        ImageView imgPreview = new ImageView();
        imgPreview.setFitWidth(400);
        imgPreview.setFitHeight(300);

        Button btnCapturar = new Button("Capturar");
        Button btnCancelar = new Button("Cancelar");

        btnCapturar.setOnAction(evento -> {
            BufferedImage imagenCapturada = webcam.getImage();
            if (imagenCapturada != null) {
                byte[] bytesFoto = convertirImagenABytes(imagenCapturada);
                if (bytesFoto != null) {
                    fotoActual = bytesFoto;
                    mostrarPreviewFoto(bytesFoto);
                }
            }
            apagarCamara();
        });

        btnCancelar.setOnAction(evento -> apagarCamara());

        HBox panelBotones = new HBox(10, btnCapturar, btnCancelar);
        panelBotones.setAlignment(Pos.CENTER);

        VBox panelPrincipal = new VBox(10, imgPreview, panelBotones);
        panelPrincipal.setAlignment(Pos.CENTER);
        panelPrincipal.setPadding(new Insets(10));

        stageCamara = new Stage();
        stageCamara.setTitle("Capturar foto");
        stageCamara.setScene(new Scene(panelPrincipal));
        stageCamara.setOnCloseRequest(evento -> apagarCamara());

        timelineCamara = new Timeline(
                new KeyFrame(Duration.millis(100), evento -> {
                    BufferedImage imagen = webcam.getImage();
                    if (imagen != null) {
                        imgPreview.setImage(SwingFXUtils.toFXImage(imagen, null));
                    }
                })
        );
        timelineCamara.setCycleCount(Timeline.INDEFINITE);
        timelineCamara.play();

        stageCamara.show();
    }

    private byte[] convertirImagenABytes(BufferedImage imagen) {
        try {
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", salida);
            return salida.toByteArray();
        } catch (IOException ex) {
            mostrarMensaje("No se pudo procesar la foto capturada.");
            return null;
        }
    }

    private void apagarCamara() {
        if (timelineCamara != null) {
            timelineCamara.stop();
            timelineCamara = null;
        }

        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
        webcam = null;

        if (stageCamara != null) {
            Stage stageACerrar = stageCamara;
            stageCamara = null;
            stageACerrar.close();
        }
    }

    @FXML
    private void handleGuardar() {
        if (txtNombre.getText().isBlank() || txtApellidos.getText().isBlank() || txtCedula.getText().isBlank()) {
            mostrarMensaje("Nombre, apellidos y cedula son obligatorios.");
            return;
        }

        if (txtSalario.getText().isBlank()) {
            mostrarMensaje("Debe ingresar el salario por hora.");
            return;
        }

        Double salario;
        try {
            salario = Double.valueOf(txtSalario.getText());
        } catch (NumberFormatException ex) {
            mostrarMensaje("El salario debe ser un numero valido.");
            return;
        }

        LocalDate fechaNac = dpFechaNac.getValue();
        if (fechaNac == null) {
            mostrarMensaje("Debe ingresar la fecha de nacimiento.");
            return;
        }
        if (!fechaNac.isBefore(LocalDate.now())) {
            mostrarMensaje("La fecha de nacimiento no puede ser hoy ni en el futuro.");
            return;
        }
        if (fechaNac.isAfter(LocalDate.now().minusYears(EDAD_MINIMA))) {
            mostrarMensaje("El empleado debe ser mayor de edad (al menos " + EDAD_MINIMA + " anios).");
            return;
        }

        if (chkAdmin.isSelected() && (txtClave.getText() == null || txtClave.getText().isBlank())) {
            mostrarMensaje("Debe ingresar una clave para los administradores.");
            return;
        }

        EmpleadoDto empleado = new EmpleadoDto();
        empleado.setId(idSeleccionado);

        empleado.setNombre(txtNombre.getText());
        empleado.setApellidos(txtApellidos.getText());
        empleado.setCedula(txtCedula.getText());
        empleado.setFechaNacimiento(fechaNac);
        empleado.setSalarioPorHora(salario);
        empleado.setFoto(fotoActual);
        empleado.setClave(chkAdmin.isSelected() ? txtClave.getText() : null);
        empleado.setAdministrador(chkAdmin.isSelected());

        Respuesta respuesta = empleadoService.guardarEmpleado(empleado);

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }
        limpiarFormulario();
        cargarTabla(txtBuscar.getText());

    }

    @FXML
    private void handleEliminar() {
        EmpleadoDto seleccionado = tblEmpleados.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje("Debe seleccionar un empleado de la tabla.");
            return;
        }

        Respuesta respuesta = empleadoService.eliminarEmpleado(seleccionado.getId());

        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        cargarTabla(txtBuscar.getText());
        limpiarFormulario();
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        apagarCamara();
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
