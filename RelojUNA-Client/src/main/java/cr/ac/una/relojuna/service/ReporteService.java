package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.model.PlanillaDto;
import cr.ac.una.relojuna.util.Respuesta;
import cr.ac.una.relojuna.ws.ReporteWS;
import cr.ac.una.relojuna.ws.ReporteWS_Service;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.List;

public class ReporteService {

    private ReporteWS puerto;

    public ReporteService() {
        ReporteWS_Service servicioWS = new ReporteWS_Service();
        puerto = servicioWS.getReporteWSPort();
    }

    public Respuesta exportarConsultaExcel(List<ConsultaResultadoDto> resultados) {
        try {
            List<cr.ac.una.relojuna.ws.ConsultaFilaDto> filasServidor = new ArrayList<>();

            for (ConsultaResultadoDto fila : resultados) {
                cr.ac.una.relojuna.ws.ConsultaFilaDto filaServidor = new cr.ac.una.relojuna.ws.ConsultaFilaDto();
                filaServidor.setFolioEmpleado(fila.getFolioEmpleado());
                filaServidor.setNombreEmpleado(fila.getNombreEmpleado());
                filaServidor.setFecha(fila.getFecha().toString());
                filaServidor.setHoraEntrada(fila.getHoraEntrada().toString());
                filaServidor.setHoraSalida(fila.getHoraSalida().toString());
                filaServidor.setHorasTrabajadas(fila.getHorasTrabajadas());
                filasServidor.add(filaServidor);
            }

            cr.ac.una.relojuna.ws.ListaConsultaFilaDto listaServidor = new cr.ac.una.relojuna.ws.ListaConsultaFilaDto();
            listaServidor.getFilas().addAll(filasServidor);

            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.exportarConsultaExcel(listaServidor);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            byte[] bytesExcel = convertirABytes(respuestaServidor.getAny());

            return new Respuesta(true, "", "", "Excel", bytesExcel);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error generando el Excel de la consulta.", "exportarConsultaExcel " + ex.getMessage());
        }
    }

    public Respuesta exportarPlanillaExcel(List<PlanillaDto> planillas) {
        try {
            List<cr.ac.una.relojuna.ws.PlanillaFilaDto> filasServidor = new ArrayList<>();

            for (PlanillaDto fila : planillas) {
                cr.ac.una.relojuna.ws.PlanillaFilaDto filaServidor = new cr.ac.una.relojuna.ws.PlanillaFilaDto();
                filaServidor.setFolioEmpleado(fila.getFolioEmpleado());
                filaServidor.setNombreEmpleado(fila.getNombreEmpleado());
                filaServidor.setHorasOrdinarias(fila.getHorasOrdinarias());
                filaServidor.setHorasExtras(fila.getHorasExtras());
                filaServidor.setHorasDobles(fila.getHorasDobles());
                filaServidor.setSalarioMensual(fila.getSalarioMensual());
                filasServidor.add(filaServidor);
            }

            cr.ac.una.relojuna.ws.ListaPlanillaFilaDto listaServidor = new cr.ac.una.relojuna.ws.ListaPlanillaFilaDto();
            listaServidor.getFilas().addAll(filasServidor);

            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.exportarPlanillaExcel(listaServidor);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            byte[] bytesExcel = convertirABytes(respuestaServidor.getAny());

            return new Respuesta(true, "", "", "Excel", bytesExcel);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error generando el Excel de la planilla.", "exportarPlanillaExcel " + ex.getMessage());
        }
    }

    public Respuesta generarReporteEmpleadosPdf(List<EmpleadoDto> empleados) {
        try {
            List<cr.ac.una.relojuna.ws.EmpleadoDto> empleadosServidor = new ArrayList<>();

            for (EmpleadoDto empleado : empleados) {
                cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor = new cr.ac.una.relojuna.ws.EmpleadoDto();
                empleadoServidor.setNombre(empleado.getNombre());
                empleadoServidor.setApellidos(empleado.getApellidos());
                empleadoServidor.setCedula(empleado.getCedula());
                empleadoServidor.setFechaNacimiento(empleado.getFechaNacimiento().toString());
                empleadoServidor.setSalarioHora(empleado.getSalarioPorHora());

                Integer esAdmin = 0;
                if (empleado.isAdministrador()) {
                    esAdmin = 1;
                }
                empleadoServidor.setEsAdmin(esAdmin);

                empleadosServidor.add(empleadoServidor);
            }

            cr.ac.una.relojuna.ws.ListaEmpleadoDto listaServidor = new cr.ac.una.relojuna.ws.ListaEmpleadoDto();
            listaServidor.getEmpleados().addAll(empleadosServidor);

            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.generarReporteEmpleadosPdf(listaServidor);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            byte[] bytesPdf = convertirABytes(respuestaServidor.getAny());

            return new Respuesta(true, "", "", "Pdf", bytesPdf);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error generando el reporte de empleados.", "generarReporteEmpleadosPdf " + ex.getMessage());
        }
    }

    public Respuesta generarReporteMarcasPdf(List<ConsultaResultadoDto> resultados) {
        try {
            List<cr.ac.una.relojuna.ws.ConsultaFilaDto> filasServidor = new ArrayList<>();

            for (ConsultaResultadoDto fila : resultados) {
                cr.ac.una.relojuna.ws.ConsultaFilaDto filaServidor = new cr.ac.una.relojuna.ws.ConsultaFilaDto();
                filaServidor.setFolioEmpleado(fila.getFolioEmpleado());
                filaServidor.setNombreEmpleado(fila.getNombreEmpleado());
                filaServidor.setFecha(fila.getFecha().toString());
                filaServidor.setHoraEntrada(fila.getHoraEntrada().toString());
                filaServidor.setHoraSalida(fila.getHoraSalida().toString());
                filaServidor.setHorasTrabajadas(fila.getHorasTrabajadas());
                filasServidor.add(filaServidor);
            }

            cr.ac.una.relojuna.ws.ListaConsultaFilaDto listaServidor = new cr.ac.una.relojuna.ws.ListaConsultaFilaDto();
            listaServidor.getFilas().addAll(filasServidor);

            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.generarReporteMarcasPdf(listaServidor);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            byte[] bytesPdf = convertirABytes(respuestaServidor.getAny());

            return new Respuesta(true, "", "", "Pdf", bytesPdf);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error generando el reporte de marcas.", "generarReporteMarcasPdf " + ex.getMessage());
        }
    }

    //Convierte el resultado crudo del servidor a bytes, desempacando el ArchivoDto
    private byte[] convertirABytes(Object resultadoCrudo) throws Exception {
        cr.ac.una.relojuna.ws.ArchivoDto archivoServidor = convertirAArchivoDtoServidor(resultadoCrudo);
        return archivoServidor.getContenido();
    }

    //Convierte el resultado crudo que manda el servidor a un ArchivoDto
    //Puede llegar como JAXBElement, como el tipo directo, o como un nodo XML sin procesar
    private cr.ac.una.relojuna.ws.ArchivoDto convertirAArchivoDtoServidor(Object resultadoCrudo) throws Exception {
        if (resultadoCrudo instanceof JAXBElement) {
            return (cr.ac.una.relojuna.ws.ArchivoDto) ((JAXBElement<?>) resultadoCrudo).getValue();
        }

        if (resultadoCrudo instanceof cr.ac.una.relojuna.ws.ArchivoDto) {
            return (cr.ac.una.relojuna.ws.ArchivoDto) resultadoCrudo;
        }

        if (resultadoCrudo instanceof Element) {
            JAXBContext contexto = JAXBContext.newInstance(cr.ac.una.relojuna.ws.ArchivoDto.class);
            Unmarshaller desempacador = contexto.createUnmarshaller();
            JAXBElement<cr.ac.una.relojuna.ws.ArchivoDto> elemento = desempacador.unmarshal((Element) resultadoCrudo, cr.ac.una.relojuna.ws.ArchivoDto.class);
            return elemento.getValue();
        }

        throw new Exception("No se pudo interpretar el resultado del servidor");
    }
}