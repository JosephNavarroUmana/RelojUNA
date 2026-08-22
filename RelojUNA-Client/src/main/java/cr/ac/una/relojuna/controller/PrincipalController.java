package cr.ac.una.relojuna.controller;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.util.FlowController;
import cr.ac.una.relojuna.util.SesionTemporal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrincipalController {

    @FXML
    private Label lblUsuario;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnMarcas;
    @FXML
    private Button btnMantenimientoMarcas;
    @FXML
    private Button btnPlanilla;
    @FXML
    private Button btnConsulta;
    @FXML
    private Button btnReportes;
    @FXML
    private Button btnCerrarSesion;

    @FXML
    private void initialize() {
        // Mostramos el nombre del empleado que inicio sesion
        EmpleadoDto empleado = SesionTemporal.getInstancia().getEmpleadoActual();

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
        // Limpiamos la sesion y regresamos a la pantalla de login
        SesionTemporal.getInstancia().cerrarSesion();
        FlowController.getInstancia().irAVista("LoginView.fxml", "Reloj Marcador");
    }
}