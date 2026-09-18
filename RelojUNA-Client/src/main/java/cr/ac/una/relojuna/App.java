package cr.ac.una.relojuna;

import cr.ac.una.relojuna.util.FlowController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stagePrincipal) throws Exception {
        FlowController.getInstancia().inicializar(stagePrincipal);
        FlowController.getInstancia().irALogin("LoginView.fxml", "RelojUNA — Inicio de Sesión");
    }

    public static void main(String[] args) {
        launch(args);
    }
}