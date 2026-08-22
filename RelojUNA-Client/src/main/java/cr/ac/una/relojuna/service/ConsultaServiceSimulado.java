package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.MarcaDto;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsultaServiceSimulado implements IConsultaService {

    @Override
    public List<ConsultaResultadoDto> consultarMarcas(LocalDate fechaDesde, LocalDate fechaHasta, Integer folioEmpleado) {
        List<MarcaDto> todasLasMarcas = MarcaServiceSimulado.obtenerTodasLasMarcas();

        // Filtramos las marcas por rango de fechas y por empleado usando un stream
        List<MarcaDto> marcasFiltradas = todasLasMarcas.stream()
                .filter(marca -> {
                    LocalDate fechaMarca = marca.getFechaHora().toLocalDate();
                    boolean despuesDeDesde = fechaMarca.isEqual(fechaDesde) || fechaMarca.isAfter(fechaDesde);
                    boolean antesDeHasta = fechaMarca.isEqual(fechaHasta) || fechaMarca.isBefore(fechaHasta);
                    return despuesDeDesde && antesDeHasta;
                })
                .filter(marca -> folioEmpleado == null || marca.getFolioEmpleado().equals(folioEmpleado))
                .sorted((marca1, marca2) -> marca1.getFechaHora().compareTo(marca2.getFechaHora()))
                .collect(Collectors.toList());

        // Agrupamos las marcas filtradas por empleado y por dia, para armar cada fila del resultado
        Map<String, List<MarcaDto>> marcasPorEmpleadoYDia = new HashMap<>();

        for (MarcaDto marca : marcasFiltradas) {
            String llave = marca.getFolioEmpleado() + "_" + marca.getFechaHora().toLocalDate();
            List<MarcaDto> listaDelGrupo = marcasPorEmpleadoYDia.get(llave);

            if (listaDelGrupo == null) {
                listaDelGrupo = new ArrayList<>();
                marcasPorEmpleadoYDia.put(llave, listaDelGrupo);
            }

            listaDelGrupo.add(marca);
        }

        List<ConsultaResultadoDto> resultado = new ArrayList<>();

        for (String llave : marcasPorEmpleadoYDia.keySet()) {
            List<MarcaDto> marcasDelGrupo = marcasPorEmpleadoYDia.get(llave);
            ConsultaResultadoDto fila = armarFila(marcasDelGrupo);

            if (fila != null) {
                resultado.add(fila);
            }
        }

        // Ordenamos el resultado final por fecha usando un stream
        List<ConsultaResultadoDto> resultadoOrdenado = resultado.stream()
                .sorted((fila1, fila2) -> fila1.getFecha().compareTo(fila2.getFecha()))
                .collect(Collectors.toList());

        return resultadoOrdenado;
    }

    // Arma una fila de resultado a partir de las marcas de un empleado en un dia especifico
    private ConsultaResultadoDto armarFila(List<MarcaDto> marcasDelGrupo) {
        LocalDateTime primeraEntrada = null;
        LocalDateTime ultimaSalida = null;

        for (MarcaDto marca : marcasDelGrupo) {
            if (marca.getTipo().equals("ENTRADA")) {
                if (primeraEntrada == null || marca.getFechaHora().isBefore(primeraEntrada)) {
                    primeraEntrada = marca.getFechaHora();
                }
            }
            if (marca.getTipo().equals("SALIDA")) {
                if (ultimaSalida == null || marca.getFechaHora().isAfter(ultimaSalida)) {
                    ultimaSalida = marca.getFechaHora();
                }
            }
        }

        if (primeraEntrada == null || ultimaSalida == null) {
            return null;
        }

        MarcaDto primeraMarca = marcasDelGrupo.get(0);
        double horasTrabajadas = Duration.between(primeraEntrada, ultimaSalida).toMinutes() / 60.0;

        ConsultaResultadoDto fila = new ConsultaResultadoDto();
        fila.setFolioEmpleado(primeraMarca.getFolioEmpleado());
        fila.setNombreEmpleado(primeraMarca.getNombreEmpleado());
        fila.setFecha(primeraEntrada.toLocalDate());
        fila.setHoraEntrada(primeraEntrada.toLocalTime());
        fila.setHoraSalida(ultimaSalida.toLocalTime());
        fila.setHorasTrabajadas(horasTrabajadas);

        return fila;
    }
}