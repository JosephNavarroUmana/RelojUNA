package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.PlanillaDto;
import cr.ac.una.relojuna.service.IPlanillaService;
import cr.ac.una.relojuna.service.ServiceFactory;
import cr.ac.una.relojuna.util.ExcelExportador;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PlanillaController {

    @FXML
    private TextField txtAnio;
    @FXML
    private ComboBox<String> cmbMes;
    @FXML
    private TableView<PlanillaDto> tblPlanilla;
    @FXML
    private TableColumn<PlanillaDto, String> colEmpleado;
    @FXML
    private TableColumn<PlanillaDto, Double> colHorasOrdinarias, colHorasExtras, colHorasDobles, colSalarioMensual;
    @FXML
    private Button btnGenerar, btnExportarExcel;
    @FXML
    private Button btnRegresar;

    // Servicio de planillas, se obtiene por medio de la fabrica
    private IPlanillaService planillaService;

    // Lista observable que alimenta la tabla
    private ObservableList<PlanillaDto> listaPlanilla;

    // Nombres de los meses en el mismo orden que su numero, enero es la posicion 0
    private String[] nombresMeses = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"
    };

    @FXML
    private void initialize() {
        planillaService = ServiceFactory.getPlanillaService();
        listaPlanilla = FXCollections.observableArrayList();

        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        colHorasOrdinarias.setCellValueFactory(new PropertyValueFactory<>("horasOrdinarias"));
        colHorasExtras.setCellValueFactory(new PropertyValueFactory<>("horasExtras"));
        colHorasDobles.setCellValueFactory(new PropertyValueFactory<>("horasDobles"));
        colSalarioMensual.setCellValueFactory(new PropertyValueFactory<>("salarioMensual"));

        tblPlanilla.setItems(listaPlanilla);

        cmbMes.setItems(FXCollections.observableArrayList(nombresMeses));

        // Dejamos el anio y mes actual como valores por defecto
        txtAnio.setText(String.valueOf(LocalDate.now().getYear()));
        int mesActual = LocalDate.now().getMonthValue();
        cmbMes.setValue(nombresMeses[mesActual - 1]);
    }

    @FXML
    private void handleGenerar() {
        if (txtAnio.getText() == null || txtAnio.getText().isBlank()) {
            mostrarMensaje("Debe ingresar el anio.");
            return;
        }

        if (cmbMes.getValue() == null) {
            mostrarMensaje("Debe seleccionar el mes.");
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(txtAnio.getText());
        } catch (NumberFormatException ex) {
            mostrarMensaje("El anio debe ser un numero valido.");
            return;
        }

        // Buscamos la posicion del mes seleccionado dentro del arreglo de nombres
        int mes = 0;
        for (int i = 0; i < nombresMeses.length; i++) {
            if (nombresMeses[i].equals(cmbMes.getValue())) {
                mes = i + 1;
                break;
            }
        }

        List<PlanillaDto> planilla = planillaService.generarPlanilla(anio, mes);
        listaPlanilla.clear();
        listaPlanilla.addAll(planilla);
    }

   @FXML
private void handleExportarExcel() {
    if (listaPlanilla.isEmpty()) {
        mostrarMensaje("Debe generar la planilla antes de exportar.");
        return;
    }

    FileChooser selector = new FileChooser();
    selector.setTitle("Guardar planilla como Excel");
    selector.setInitialFileName("Planilla.xlsx");
    selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx"));

    File archivo = selector.showSaveDialog(btnExportarExcel.getScene().getWindow());

    if (archivo == null) {
        return;
    }

    try {
        ExcelExportador.exportarPlanilla(listaPlanilla, archivo);
        mostrarMensaje("Archivo exportado correctamente.");
    } catch (IOException ex) {
        mostrarMensaje("Ocurrio un error al exportar el archivo.");
    }
}

    @FXML
    private void handleRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}