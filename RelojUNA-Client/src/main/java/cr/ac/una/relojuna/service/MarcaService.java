package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.util.Respuesta;
import cr.ac.una.relojuna.ws.MarcaWS;
import cr.ac.una.relojuna.ws.MarcaWS_Service;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

public class MarcaService {

    private MarcaWS puerto;

    public MarcaService() {
        MarcaWS_Service servicioWS = new MarcaWS_Service();
        puerto = servicioWS.getMarcaWSPort();
    }

    public Respuesta marcar(Long idEmpleado) {
        try {
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.marcar(idEmpleado);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            Object resultadoCrudo = respuestaServidor.getAny();
            cr.ac.una.relojuna.ws.MarcaDto marcaServidor = convertirAMarcaDtoServidor(resultadoCrudo);

            MarcaDto marca = convertirAMarcaCliente(marcaServidor);

            return new Respuesta(true, "", "", "Marca", marca);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error registrando la marca.", "marcar " + ex.getMessage());
        }
    }

    public Respuesta guardarMarca(MarcaDto marca) {
        try {
            cr.ac.una.relojuna.ws.MarcaDto marcaServidor = convertirAMarcaServidor(marca);
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.guardarMarca(marcaServidor);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            Object resultadoCrudo = respuestaServidor.getAny();
            cr.ac.una.relojuna.ws.MarcaDto marcaGuardadaServidor = convertirAMarcaDtoServidor(resultadoCrudo);

            MarcaDto marcaGuardada = convertirAMarcaCliente(marcaGuardadaServidor);

            return new Respuesta(true, "", "", "Marca", marcaGuardada);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error guardando la marca.", "guardarMarca " + ex.getMessage());
        }
    }

    public Respuesta eliminarMarca(Integer id) {
        try {
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.eliminarMarca(Long.valueOf(id));

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error eliminando la marca.", "eliminarMarca " + ex.getMessage());
        }
    }

    public Respuesta obtenerMarcas() {
        try {
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.obtenerMarcas();

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            Object resultadoCrudo = respuestaServidor.getAny();
            cr.ac.una.relojuna.ws.ListaMarcaDto listaEnvoltorio = convertirAListaMarcaDto(resultadoCrudo);

            List<cr.ac.una.relojuna.ws.MarcaDto> marcasServidor = listaEnvoltorio.getMarcas();

            List<MarcaDto> marcas = new ArrayList<>();
            for (cr.ac.una.relojuna.ws.MarcaDto marcaServidor : marcasServidor) {
                marcas.add(convertirAMarcaCliente(marcaServidor));
            }

            return new Respuesta(true, "", "", "Marcas", marcas);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error obteniendo las marcas.", "obtenerMarcas " + ex.getMessage());
        }
    }

    private Respuesta obtenerInconsistencias() {
        try {
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.buscarInconsistencias();

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            Object resultadoCrudo = respuestaServidor.getAny();
            cr.ac.una.relojuna.ws.ListaMarcaDto listaEnvoltorio = convertirAListaMarcaDto(resultadoCrudo);

            List<cr.ac.una.relojuna.ws.MarcaDto> marcasServidor = listaEnvoltorio.getMarcas();

            List<MarcaDto> marcas = new ArrayList<>();
            for (cr.ac.una.relojuna.ws.MarcaDto marcaServidor : marcasServidor) {
                marcas.add(convertirAMarcaCliente(marcaServidor));
            }

            return new Respuesta(true, "", "", "Marcas", marcas);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error buscando las inconsistencias.", "obtenerInconsistencias " + ex.getMessage());
        }
    }

    public static List<MarcaDto> obtenerTodasLasMarcas() {
        try {
            MarcaService servicioTemporal = new MarcaService();
            Respuesta respuesta = servicioTemporal.obtenerMarcas();

            if (!respuesta.getEstado()) {
                return new ArrayList<>();
            }

            return (List<MarcaDto>) respuesta.getResultado("Marcas");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Respuesta buscarMarcas(LocalDate fechaDesde, LocalDate fechaHasta) {
        try {
            Respuesta respuestaTodas = obtenerMarcas();

            if (!respuestaTodas.getEstado()) {
                return respuestaTodas;
            }

            List<MarcaDto> todasLasMarcas = (List<MarcaDto>) respuestaTodas.getResultado("Marcas");
            List<MarcaDto> resultado = new ArrayList<>();

            for (MarcaDto marca : todasLasMarcas) {
                LocalDate fechaMarca = marca.getFechaHora().toLocalDate();

                boolean despuesDeDesde = fechaMarca.isEqual(fechaDesde) || fechaMarca.isAfter(fechaDesde);
                boolean antesDeHasta = fechaMarca.isEqual(fechaHasta) || fechaMarca.isBefore(fechaHasta);

                if (despuesDeDesde && antesDeHasta) {
                    resultado.add(marca);
                }
            }

            return new Respuesta(true, "", "", "Marcas", resultado);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error buscando las marcas.", "buscarMarcas " + ex.getMessage());
        }
    }

    public Respuesta buscarInconsistencias(LocalDate fechaDesde, LocalDate fechaHasta) {
        try {
            Respuesta respuestaTodas = obtenerInconsistencias();

            if (!respuestaTodas.getEstado()) {
                return respuestaTodas;
            }

            List<MarcaDto> todasLasInconsistencias = (List<MarcaDto>) respuestaTodas.getResultado("Marcas");
            List<MarcaDto> resultado = new ArrayList<>();

            for (MarcaDto marca : todasLasInconsistencias) {
                LocalDate fechaMarca = marca.getFechaHora().toLocalDate();

                boolean despuesDeDesde = fechaMarca.isEqual(fechaDesde) || fechaMarca.isAfter(fechaDesde);
                boolean antesDeHasta = fechaMarca.isEqual(fechaHasta) || fechaMarca.isBefore(fechaHasta);

                if (despuesDeDesde && antesDeHasta) {
                    resultado.add(marca);
                }
            }

            return new Respuesta(true, "", "", "Marcas", resultado);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta(false, "Error buscando las inconsistencias.", "buscarInconsistencias " + ex.getMessage());
        }
    }

    private MarcaDto convertirAMarcaCliente(cr.ac.una.relojuna.ws.MarcaDto marcaServidor) {
        MarcaDto marca = new MarcaDto();
        marca.setId(marcaServidor.getId().intValue());
        marca.setFechaHora(LocalDateTime.parse(marcaServidor.getHora().toString()));
        marca.setTipo(marcaServidor.getTipo());
        marca.setEstado("OK");

        if (marcaServidor.getFolioEmpleado() != null) {
            marca.setFolioEmpleado(marcaServidor.getFolioEmpleado());
        }

        marca.setNombreEmpleado(marcaServidor.getNombreEmpleado());

        return marca;
    }

    private cr.ac.una.relojuna.ws.MarcaDto convertirAMarcaServidor(MarcaDto marca) {
        cr.ac.una.relojuna.ws.MarcaDto marcaServidor = new cr.ac.una.relojuna.ws.MarcaDto();

        if (marca.getId() != null) {
            Long id = Long.valueOf(marca.getId());
            marcaServidor.setId(id);
        }

        marcaServidor.setFecha(marca.getFechaHora().toLocalDate().toString());
        marcaServidor.setHora(marca.getFechaHora().toString());
        marcaServidor.setTipo(marca.getTipo());
        marcaServidor.setFolioEmpleado(marca.getFolioEmpleado().toString());

        return marcaServidor;
    }

    private cr.ac.una.relojuna.ws.MarcaDto convertirAMarcaDtoServidor(Object resultadoCrudo) throws Exception {
        if (resultadoCrudo instanceof JAXBElement) {
            return (cr.ac.una.relojuna.ws.MarcaDto) ((JAXBElement<?>) resultadoCrudo).getValue();
        }

        if (resultadoCrudo instanceof cr.ac.una.relojuna.ws.MarcaDto) {
            return (cr.ac.una.relojuna.ws.MarcaDto) resultadoCrudo;
        }

        if (resultadoCrudo instanceof Element) {
            JAXBContext contexto = JAXBContext.newInstance(cr.ac.una.relojuna.ws.MarcaDto.class);
            Unmarshaller desempacador = contexto.createUnmarshaller();
            JAXBElement<cr.ac.una.relojuna.ws.MarcaDto> elemento = desempacador.unmarshal((Element) resultadoCrudo, cr.ac.una.relojuna.ws.MarcaDto.class);
            return elemento.getValue();
        }

        throw new Exception("No se pudo interpretar el resultado del servidor");
    }

    private cr.ac.una.relojuna.ws.ListaMarcaDto convertirAListaMarcaDto(Object resultadoCrudo) throws Exception {
        if (resultadoCrudo instanceof JAXBElement) {
            return (cr.ac.una.relojuna.ws.ListaMarcaDto) ((JAXBElement<?>) resultadoCrudo).getValue();
        }

        if (resultadoCrudo instanceof cr.ac.una.relojuna.ws.ListaMarcaDto) {
            return (cr.ac.una.relojuna.ws.ListaMarcaDto) resultadoCrudo;
        }

        if (resultadoCrudo instanceof org.w3c.dom.Element) {
            JAXBContext contexto = JAXBContext.newInstance(cr.ac.una.relojuna.ws.ListaMarcaDto.class);
            Unmarshaller desempacador = contexto.createUnmarshaller();
            JAXBElement<cr.ac.una.relojuna.ws.ListaMarcaDto> elemento = desempacador.unmarshal((org.w3c.dom.Element) resultadoCrudo, cr.ac.una.relojuna.ws.ListaMarcaDto.class);
            return elemento.getValue();
        }

        throw new Exception("No se pudo interpretar el resultado del servidor");
    }
}
