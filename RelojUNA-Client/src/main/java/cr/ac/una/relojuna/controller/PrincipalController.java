package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.util.AppContext;
import cr.ac.una.relojuna.util.FlowController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PrincipalController {

    @FXML
    private Label lblUsuario;

    @FXML
    private void initialize() {
        EmpleadoDto empleado = (EmpleadoDto) AppContext.getInstance().get("Usuario");

        if (empleado != null) {
            lblUsuario.setText("Bienvenido: " + empleado.getNombre() + " " + empleado.getApellidos());
        }
    }

    @FXML
    private void handleEmpleados() {
        FlowController.getInstancia().abrirVistaModal("EmpleadoView.fxml", "Mantenimiento de Empleados");
    }

    @FXML
    private void handleMarcas() {
        FlowController.getInstancia().abrirVistaModal("MarcaView.fxml", "Pantalla de Marcas");
    }

    @FXML
    private void handleMantenimientoMarcas() {
        FlowController.getInstancia().abrirVistaModal("MantenimientoMarcasView.fxml", "Mantenimiento de Marcas");
    }

    @FXML
    private void handlePlanilla() {
        FlowController.getInstancia().abrirVistaModal("PlanillaView.fxml", "Generacion de Planillas");
    }

    @FXML
    private void handleConsulta() {
        FlowController.getInstancia().abrirVistaModal("ConsultaView.fxml", "Consultas con Streams");
    }

    @FXML
    private void handleReportes() {
        FlowController.getInstancia().abrirVistaModal("ReporteView.fxml", "Modulo de Reportes");
    }

    @FXML
    private void handleCerrarSesion() {
        AppContext.getInstance().delete("Usuario");
        FlowController.getInstancia().irAVista("LoginView.fxml", "Reloj Marcador");
    }
}
