package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.ConsultaResultadoDto;
import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.util.Respuesta;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsultaService {

    //Consulta marcas usando streams
    public Respuesta consultarMarcas(LocalDate fechaDesde, LocalDate fechaHasta, String folioEmpleado) {
        try {
            List<MarcaDto> todasLasMarcas = MarcaService.obtenerTodasLasMarcas();

            List<MarcaDto> marcasFiltradas = todasLasMarcas.stream()
                    .filter(marca -> {
                        LocalDate fechaMarca = marca.getFechaHora().toLocalDate();
                        boolean despuesDeDesde = fechaMarca.isEqual(fechaDesde) || fechaMarca.isAfter(fechaDesde);
                        boolean antesDeHasta = fechaMarca.isEqual(fechaHasta) || fechaMarca.isBefore(fechaHasta);

                        boolean coincideEmpleado = true;
                        if (folioEmpleado != null) {
                            coincideEmpleado = marca.getFolioEmpleado().equals(folioEmpleado);
                        }

                        return despuesDeDesde && antesDeHasta && coincideEmpleado;
                    })
                    .sorted((marca1, marca2) -> marca1.getFechaHora().compareTo(marca2.getFechaHora()))
                    .collect(Collectors.toList());

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

            List<ConsultaResultadoDto> resultadoOrdenado = resultado.stream()
                    .sorted((fila1, fila2) -> fila1.getFecha().compareTo(fila2.getFecha()))
                    .collect(Collectors.toList());

            return new Respuesta(true, "", "", "Consultas", resultadoOrdenado);
        } catch (Exception ex) {
            return new Respuesta(false, "Error consultando las marcas.", "consultarMarcas " + ex.getMessage());
        }
    }

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
