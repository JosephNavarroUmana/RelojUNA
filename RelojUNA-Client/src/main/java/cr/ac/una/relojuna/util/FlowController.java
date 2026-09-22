package cr.ac.una.relojuna.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FlowController {

    private static final double LOGIN_ANCHO = 480;
    private static final double LOGIN_ALTO = 620;

    private static final double APP_ANCHO = 1180;
    private static final double APP_ALTO = 760;
    private static final double APP_MIN_ANCHO = 1000;
    private static final double APP_MIN_ALTO = 660;

    private static FlowController instancia;
    private Stage stagePrincipal;

    private boolean appDimensionada = false;

    private FlowController() {
    }

    public static FlowController getInstancia() {
        if (instancia == null) {
            instancia = new FlowController();
        }
        return instancia;
    }

    public void inicializar(Stage stage) {
        this.stagePrincipal = stage;
    }

    public void irALogin(String nombreFxml, String titulo) {
        try {
            Parent raiz = cargarFxml(nombreFxml);
            reemplazarRoot(raiz);

            stagePrincipal.setTitle(titulo);
            stagePrincipal.setResizable(false);
            stagePrincipal.setMinWidth(LOGIN_ANCHO);
            stagePrincipal.setMinHeight(LOGIN_ALTO);
            stagePrincipal.setWidth(LOGIN_ANCHO);
            stagePrincipal.setHeight(LOGIN_ALTO);
            stagePrincipal.centerOnScreen();

            if (!stagePrincipal.isShowing()) {
                stagePrincipal.show();
            }

            appDimensionada = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void irAVista(String nombreFxml, String titulo) {
        try {
            Parent raiz = cargarFxml(nombreFxml);
            reemplazarRoot(raiz);

            if (!appDimensionada) {
                stagePrincipal.setResizable(true);
                stagePrincipal.setMinWidth(APP_MIN_ANCHO);
                stagePrincipal.setMinHeight(APP_MIN_ALTO);
                stagePrincipal.setWidth(APP_ANCHO);
                stagePrincipal.setHeight(APP_ALTO);
                stagePrincipal.centerOnScreen();
                appDimensionada = true;
            }

            stagePrincipal.setTitle(titulo);

            if (!stagePrincipal.isShowing()) {
                stagePrincipal.show();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void abrirVistaModal(String nombreFxml, String titulo) {
        try {
            Parent raiz = cargarFxml(nombreFxml);

            Stage stageModal = new Stage();
            stageModal.setTitle(titulo);
            stageModal.setScene(new Scene(raiz));
            stageModal.initModality(Modality.WINDOW_MODAL);
            stageModal.initOwner(stagePrincipal);
            stageModal.centerOnScreen();
            stageModal.sizeToScene();

            stageModal.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Parent cargarFxml(String nombreFxml) throws IOException {
        String ruta = "/cr/ac/una/relojuna/view/" + nombreFxml;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
        return loader.load();
    }

    private void reemplazarRoot(Parent raiz) {
        if (stagePrincipal.getScene() == null) {
            stagePrincipal.setScene(new Scene(raiz));
        } else {
            stagePrincipal.getScene().setRoot(raiz);
        }
    }
}
