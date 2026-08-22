package cr.ac.una.relojuna.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FlowController {

    // Instancia unica de la clase (patron singleton)
    private static FlowController instancia;

    // Ventana principal de la aplicacion
    private Stage stagePrincipal;

    private FlowController() {
    }

    public static FlowController getInstancia() {
        if (instancia == null) {
            instancia = new FlowController();
        }
        return instancia;
    }

    // Guarda la referencia del stage principal, se llama una sola vez desde MainApp
    public void inicializar(Stage stage) {
        this.stagePrincipal = stage;
    }

    // Cambia la escena completa del stage principal por una vista nueva
    public void irAVista(String nombreFxml, String titulo) {
        try {
            String ruta = "/cr/ac/una/relojuna/view/" + nombreFxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent raiz = loader.load();

            Scene escena = new Scene(raiz);
            stagePrincipal.setScene(escena);
            stagePrincipal.setTitle(titulo);
            stagePrincipal.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Abre una vista en una ventana modal, bloquea la ventana de atras hasta que la cierren
    public void abrirVistaModal(String nombreFxml, String titulo) {
        try {
            String ruta = "/cr/ac/una/relojuna/view/" + nombreFxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent raiz = loader.load();

            Stage stageModal = new Stage();
            stageModal.setTitle(titulo);
            stageModal.setScene(new Scene(raiz));

            // Bloquea la interaccion con la ventana principal mientras esta abierta
            stageModal.initModality(Modality.WINDOW_MODAL);
            stageModal.initOwner(stagePrincipal);

            // Espera a que la cierren antes de continuar
            stageModal.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}