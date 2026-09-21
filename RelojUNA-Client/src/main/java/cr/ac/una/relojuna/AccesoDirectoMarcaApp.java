package cr.ac.una.relojuna;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccesoDirectoMarcaApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/relojuna/view/MarcaView.fxml"));
        Parent raiz = loader.load();

        stage.setTitle("RelojUNA - Registro de Marcas");
        stage.setScene(new Scene(raiz));
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}