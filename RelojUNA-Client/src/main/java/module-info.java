module cr.ac.una.relojuna {
    requires javafx.controls;
    requires javafx.fxml;
//    requires materialfx;
//    requires java.logging;
//    requires jakarta.xml.ws;
    requires org.apache.poi.ooxml;
//    requires jasperreports;
    opens cr.ac.una.relojuna.model to javafx.base;
    opens cr.ac.una.relojuna.controller to javafx.fxml;
    exports cr.ac.una.relojuna;
}
