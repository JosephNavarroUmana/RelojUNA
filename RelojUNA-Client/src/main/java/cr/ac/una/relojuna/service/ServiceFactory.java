package cr.ac.una.relojuna.service;

public class ServiceFactory {

    // Bandera para decidir si usamos los servicios simulados o los reales
    // Por ahora queda en false porque los servicios reales todavia no estan listos
    private static boolean usarServiciosReales = false;

    public static IEmpleadoService getEmpleadoService() {
        if (usarServiciosReales) {
            return new EmpleadoServiceWS();
        } else {
            return new EmpleadoServiceSimulado();
        }
    }

    public static ILoginService getLoginService() {
        if (usarServiciosReales) {
            return new LoginServiceWS();
        } else {
            return new LoginServiceSimulado();
        }
    }

    public static IMarcaService getMarcaService() {
        if (usarServiciosReales) {
            return new MarcaServiceWS();
        } else {
            return new MarcaServiceSimulado();
        }
    }

    public static IPlanillaService getPlanillaService() {
        if (usarServiciosReales) {
            return new PlanillaServiceWS();
        } else {
            return new PlanillaServiceSimulado();
        }
    }

    public static IConsultaService getConsultaService() {
        if (usarServiciosReales) {
            return new ConsultaServiceWS();
        } else {
            return new ConsultaServiceSimulado();
        }
    }
}