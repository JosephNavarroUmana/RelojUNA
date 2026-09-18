package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.PlanillaDto;
import cr.ac.una.relojuna.util.Respuesta;
import cr.ac.una.relojuna.ws.PlanillaWS;
import cr.ac.una.relojuna.ws.PlanillaWS_Service;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

public class PlanillaService {

    private PlanillaWS puerto;

    public PlanillaService() {
        PlanillaWS_Service servicioWS = new PlanillaWS_Service();
        puerto = servicioWS.getPlanillaWSPort();
    }

    //Pide al servidor que genere la planilla del mes y anio indicados
    public Respuesta generarPlanilla(int anio, int mes) {
        try {
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.generarPlanilla(mes, anio);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            Object resultadoCrudo = respuestaServidor.getAny();
            cr.ac.una.relojuna.ws.ListaPlanillaFilaDto listaEnvoltorio = convertirAListaPlanillaFilaDto(resultadoCrudo);

            List<cr.ac.una.relojuna.ws.PlanillaFilaDto> filasServidor = listaEnvoltorio.getFilas();

            List<PlanillaDto> resultado = new ArrayList<>();
            for (cr.ac.una.relojuna.ws.PlanillaFilaDto filaServidor : filasServidor) {
                resultado.add(convertirAPlanillaCliente(filaServidor));
            }

            return new Respuesta(true, "", "", "Planilla", resultado);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error generando la planilla.", "generarPlanilla " + ex.getMessage());
        }
    }

    //Convierte una fila que llega del servidor al dto que usa el cliente
    private PlanillaDto convertirAPlanillaCliente(cr.ac.una.relojuna.ws.PlanillaFilaDto filaServidor) {
        PlanillaDto planilla = new PlanillaDto();
        planilla.setFolioEmpleado(filaServidor.getFolioEmpleado());
        planilla.setNombreEmpleado(filaServidor.getNombreEmpleado());
        planilla.setHorasOrdinarias(filaServidor.getHorasOrdinarias());
        planilla.setHorasExtras(filaServidor.getHorasExtras());
        planilla.setHorasDobles(filaServidor.getHorasDobles());
        planilla.setSalarioMensual(filaServidor.getSalarioMensual());
        return planilla;
    }

    //Convierte el resultado crudo que manda el servidor al envoltorio de la lista de filas
    //Puede llegar como JAXBElement, como el tipo directo, o como un nodo XML sin procesar
    private cr.ac.una.relojuna.ws.ListaPlanillaFilaDto convertirAListaPlanillaFilaDto(Object resultadoCrudo) throws Exception {
        if (resultadoCrudo instanceof JAXBElement) {
            return (cr.ac.una.relojuna.ws.ListaPlanillaFilaDto) ((JAXBElement<?>) resultadoCrudo).getValue();
        }

        if (resultadoCrudo instanceof cr.ac.una.relojuna.ws.ListaPlanillaFilaDto) {
            return (cr.ac.una.relojuna.ws.ListaPlanillaFilaDto) resultadoCrudo;
        }

        if (resultadoCrudo instanceof Element) {
            JAXBContext contexto = JAXBContext.newInstance(cr.ac.una.relojuna.ws.ListaPlanillaFilaDto.class);
            Unmarshaller desempacador = contexto.createUnmarshaller();
            JAXBElement<cr.ac.una.relojuna.ws.ListaPlanillaFilaDto> elemento = desempacador.unmarshal((Element) resultadoCrudo, cr.ac.una.relojuna.ws.ListaPlanillaFilaDto.class);
            return elemento.getValue();
        }

        throw new Exception("No se pudo interpretar el resultado del servidor");
    }
}