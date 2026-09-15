package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.util.Respuesta;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarcaService {

    //Lista de marcas en memoria, simula la base de datos
    private static List<MarcaDto> marcas = new ArrayList<>();

    //Contador para generar el siguiente id de marca
    private static int siguienteId = 1;

    //Registra la marca de entrada o salida de un empleado segun su folio
    public Respuesta marcar(Integer folioEmpleado) {
        try {
            EmpleadoService empleadoService = new EmpleadoService();
            Respuesta respuestaEmpleado = empleadoService.buscarEmpleados(folioEmpleado.toString());
            List<EmpleadoDto> empleadosEncontrados = (List<EmpleadoDto>) respuestaEmpleado.getResultado("Empleados");

            String nombreEmpleado = "Desconocido";
            if (empleadosEncontrados != null && !empleadosEncontrados.isEmpty()) {
                EmpleadoDto empleado = empleadosEncontrados.get(0);
                nombreEmpleado = empleado.getNombre() + " " + empleado.getApellidos();
            }

            String tipoMarca = "ENTRADA";
            String ultimoTipo = buscarUltimoTipoDeMarca(folioEmpleado);
            if (ultimoTipo != null && ultimoTipo.equals("ENTRADA")) {
                tipoMarca = "SALIDA";
            }

            MarcaDto marcaNueva = new MarcaDto();
            marcaNueva.setId(siguienteId);
            siguienteId = siguienteId + 1;
            marcaNueva.setFolioEmpleado(folioEmpleado);
            marcaNueva.setNombreEmpleado(nombreEmpleado);
            marcaNueva.setFechaHora(LocalDateTime.now());
            marcaNueva.setTipo(tipoMarca);
            marcaNueva.setEstado("OK");

            marcas.add(marcaNueva);

            return new Respuesta(true, "", "", "Marca", marcaNueva);
        } catch (Exception ex) {
            return new Respuesta(false, "Error registrando la marca.", "marcar " + ex.getMessage());
        }
    }

    //Busca el tipo de la ultima marca registrada de un empleado
    private String buscarUltimoTipoDeMarca(Integer folioEmpleado) {
        MarcaDto ultimaMarca = null;

        for (MarcaDto marca : marcas) {
            if (marca.getFolioEmpleado().equals(folioEmpleado)) {
                if (ultimaMarca == null || marca.getFechaHora().isAfter(ultimaMarca.getFechaHora())) {
                    ultimaMarca = marca;
                }
            }
        }

        if (ultimaMarca == null) {
            return null;
        }

        return ultimaMarca.getTipo();
    }

    //Busca las marcas entre dos fechas
    public Respuesta buscarMarcas(LocalDate fechaDesde, LocalDate fechaHasta) {
        try {
            //Recalculamos los estados de todas las marcas antes de filtrar, asi siempre estan al dia
            recalcularEstados();

            List<MarcaDto> resultado = new ArrayList<>();

            for (MarcaDto marca : marcas) {
                LocalDate fechaMarca = marca.getFechaHora().toLocalDate();

                boolean despuesDeDesde = fechaMarca.isEqual(fechaDesde) || fechaMarca.isAfter(fechaDesde);
                boolean antesDeHasta = fechaMarca.isEqual(fechaHasta) || fechaMarca.isBefore(fechaHasta);

                if (despuesDeDesde && antesDeHasta) {
                    resultado.add(marca);
                }
            }

            return new Respuesta(true, "", "", "Marcas", resultado);
        } catch (Exception ex) {
            return new Respuesta(false, "Error buscando las marcas.", "buscarMarcas " + ex.getMessage());
        }
    }

    //Busca solo las marcas que tienen alguna inconsistencia entre dos fechas
    public Respuesta buscarInconsistencias(LocalDate fechaDesde, LocalDate fechaHasta) {
        try {
            Respuesta respuestaMarcas = buscarMarcas(fechaDesde, fechaHasta);
            List<MarcaDto> marcasEnRango = (List<MarcaDto>) respuestaMarcas.getResultado("Marcas");

            List<MarcaDto> inconsistencias = new ArrayList<>();

            for (MarcaDto marca : marcasEnRango) {
                if (marca.getEstado().equals("INCONSISTENTE")) {
                    inconsistencias.add(marca);
                }
            }

            return new Respuesta(true, "", "", "Marcas", inconsistencias);
        } catch (Exception ex) {
            return new Respuesta(false, "Error buscando las inconsistencias.", "buscarInconsistencias " + ex.getMessage());
        }
    }

    //Revisa todas las marcas de todos los empleados y actualiza el estado de cada una
    private void recalcularEstados() {
        //Primero ponemos todo en OK, para no arrastrar inconsistencias viejas ya corregidas
        for (MarcaDto marca : marcas) {
            marca.setEstado("OK");
        }

        Map<Integer, List<MarcaDto>> marcasPorEmpleado = new HashMap<>();

        for (MarcaDto marca : marcas) {
            List<MarcaDto> listaDelEmpleado = marcasPorEmpleado.get(marca.getFolioEmpleado());

            if (listaDelEmpleado == null) {
                listaDelEmpleado = new ArrayList<>();
                marcasPorEmpleado.put(marca.getFolioEmpleado(), listaDelEmpleado);
            }

            listaDelEmpleado.add(marca);
        }

        for (Integer folio : marcasPorEmpleado.keySet()) {
            List<MarcaDto> marcasDelEmpleado = marcasPorEmpleado.get(folio);

            List<MarcaDto> marcasOrdenadas = marcasDelEmpleado.stream()
                    .sorted((marca1, marca2) -> marca1.getFechaHora().compareTo(marca2.getFechaHora()))
                    .collect(Collectors.toList());

            for (int i = 0; i < marcasOrdenadas.size() - 1; i++) {
                MarcaDto marcaActual = marcasOrdenadas.get(i);
                MarcaDto marcaSiguiente = marcasOrdenadas.get(i + 1);

                if (marcaActual.getTipo().equals(marcaSiguiente.getTipo())) {
                    marcaActual.setEstado("INCONSISTENTE");
                    marcaSiguiente.setEstado("INCONSISTENTE");
                }
            }
        }
    }

    //Guarda una marca nueva o actualiza una existente segun el id
    public Respuesta guardarMarca(MarcaDto marca) {
        try {
            if (marca.getId() == null) {
                marca.setId(siguienteId);
                siguienteId = siguienteId + 1;
                marcas.add(marca);
                return new Respuesta(true, "", "", "Marca", marca);
            }

            for (int i = 0; i < marcas.size(); i++) {
                MarcaDto actual = marcas.get(i);
                if (actual.getId().equals(marca.getId())) {
                    marcas.set(i, marca);
                    return new Respuesta(true, "", "", "Marca", marca);
                }
            }

            marcas.add(marca);
            return new Respuesta(true, "", "", "Marca", marca);
        } catch (Exception ex) {
            return new Respuesta(false, "Error guardando la marca.", "guardarMarca " + ex.getMessage());
        }
    }

    //Elimina una marca segun su id
    public Respuesta eliminarMarca(Integer id) {
        try {
            MarcaDto marcaAEliminar = null;

            for (MarcaDto marca : marcas) {
                if (marca.getId().equals(id)) {
                    marcaAEliminar = marca;
                    break;
                }
            }

            if (marcaAEliminar != null) {
                marcas.remove(marcaAEliminar);
            }

            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            return new Respuesta(false, "Error eliminando la marca.", "eliminarMarca " + ex.getMessage());
        }
    }

    //Metodo para que Planilla y Consulta puedan leer todas las marcas sin pasar por fechas
    public static List<MarcaDto> obtenerTodasLasMarcas() {
        return marcas;
    }
}