package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.model.MarcaDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarcaServiceSimulado implements IMarcaService {

    //Lista de marcas en memoria, simula la base de datos
    private static List<MarcaDto> marcas = new ArrayList<>();

    //Contador para generar el siguiente id de marca
    private static int siguienteId = 1;

    @Override
    public MarcaDto marcar(Integer folioEmpleado) {
        //Buscamos el nombre del empleado para guardarlo en la marca
        IEmpleadoService empleadoService = new EmpleadoServiceSimulado();
        List<EmpleadoDto> empleados = empleadoService.buscarEmpleados(folioEmpleado.toString());

        String nombreEmpleado = "Desconocido";
        if (!empleados.isEmpty()) {
            EmpleadoDto empleado = empleados.get(0);
            nombreEmpleado = empleado.getNombre() + " " + empleado.getApellidos();
        }

        //Decidimos si es entrada o salida segun la ultima marca de ese empleado
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
        return marcaNueva;
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

  @Override
public List<MarcaDto> buscarMarcas(LocalDate fechaDesde, LocalDate fechaHasta) {
    // Recalculamos los estados de todas las marcas antes de filtrar, asi siempre estan al dia
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

    return resultado;
}

@Override
public List<MarcaDto> buscarInconsistencias(LocalDate fechaDesde, LocalDate fechaHasta) {
    // Reutilizamos buscarMarcas para tener el rango ya filtrado y con estados actualizados
    List<MarcaDto> marcasEnRango = buscarMarcas(fechaDesde, fechaHasta);

    List<MarcaDto> inconsistencias = new ArrayList<>();

    for (MarcaDto marca : marcasEnRango) {
        if (marca.getEstado().equals("INCONSISTENTE")) {
            inconsistencias.add(marca);
        }
    }

    return inconsistencias;
}

// Revisa todas las marcas de todos los empleados y actualiza el estado de cada una
private void recalcularEstados() {
    // Primero ponemos todo en OK, para no arrastrar inconsistencias viejas ya corregidas
    for (MarcaDto marca : marcas) {
        marca.setEstado("OK");
    }

    // Agrupamos las marcas por folio de empleado usando un mapa
    Map<Integer, List<MarcaDto>> marcasPorEmpleado = new HashMap<>();

    for (MarcaDto marca : marcas) {
        List<MarcaDto> listaDelEmpleado = marcasPorEmpleado.get(marca.getFolioEmpleado());

        if (listaDelEmpleado == null) {
            listaDelEmpleado = new ArrayList<>();
            marcasPorEmpleado.put(marca.getFolioEmpleado(), listaDelEmpleado);
        }

        listaDelEmpleado.add(marca);
    }

    // Revisamos las marcas de cada empleado por separado
    for (Integer folio : marcasPorEmpleado.keySet()) {
        List<MarcaDto> marcasDelEmpleado = marcasPorEmpleado.get(folio);

        // Ordenamos las marcas del empleado por fecha y hora usando un stream
        List<MarcaDto> marcasOrdenadas = marcasDelEmpleado.stream()
                .sorted((marca1, marca2) -> marca1.getFechaHora().compareTo(marca2.getFechaHora()))
                .collect(Collectors.toList());

        // Comparamos cada marca con la siguiente, deben alternar ENTRADA y SALIDA
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

    @Override
    public MarcaDto guardarMarca(MarcaDto marca) {
        //Si no tiene id, es una marca nueva
        if (marca.getId() == null) {
            marca.setId(siguienteId);
            siguienteId = siguienteId + 1;
            marcas.add(marca);
            return marca;
        }

        //Si ya tiene id, buscamos la marca existente y la actualizamos
        for (int i = 0; i < marcas.size(); i++) {
            MarcaDto actual = marcas.get(i);
            if (actual.getId().equals(marca.getId())) {
                marcas.set(i, marca);
                return marca;
            }
        }

        marcas.add(marca);
        return marca;
    }

    @Override
    public void eliminarMarca(Integer id) {
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
    }

    //Metodo para que otros servicios simulados puedan consultar todas las marcas
    public static List<MarcaDto> obtenerTodasLasMarcas() {
        return marcas;
    }
}