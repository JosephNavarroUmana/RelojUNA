package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.ConsultaResultadoDto;
import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.modelo.Marca;
import cr.ac.una.relojservidor.util.EntityManagerHelper;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsultaService {

    /**
     * Consulta las marcas de un rango de fechas, opcionalmente filtradas
     * por un empleado especifico. Trae TODAS las marcas con JPQL (unica
     * consulta permitida) y hace el resto (filtro por fecha/empleado,
     * conteos, totales) solo con streams, como exige el punto 7.
     *
     * @param empleadoId opcional; si es null, se consideran todos los empleados
     */
    public Respuesta consultarMarcas(LocalDate desde, LocalDate hasta, Long empleadoId) {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            // Única consulta JPQL permitida: traer TODAS las marcas
            List<Marca> todasLasMarcas = em.createQuery(
                    "SELECT m FROM Marca m", Marca.class).getResultList();

            // Todo lo demás, con streams
            List<Marca> marcasFiltradas = todasLasMarcas.stream()
                    .filter(m -> !m.getFecha().isBefore(desde) && !m.getFecha().isAfter(hasta))
                    .filter(m -> empleadoId == null || m.getEmpleado().getId().equals(empleadoId))
                    .sorted(Comparator.comparing(Marca::getHora))
                    .collect(Collectors.toList());

            List<MarcaDto> dtos = marcasFiltradas.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());

            long cantidadEmpleados = marcasFiltradas.stream()
                    .map(m -> m.getEmpleado().getId())
                    .distinct()
                    .count();

            long totalMarcas = marcasFiltradas.size();

            double totalHoras = calcularTotalHorasTrabajadas(marcasFiltradas);

            ConsultaResultadoDto resultado = new ConsultaResultadoDto(dtos, cantidadEmpleados, totalMarcas, totalHoras);

             return new Respuesta(true, "Consulta realizada con éxito", resultado);
        } catch (Exception e) {
            return new Respuesta(false, "Error al consultar marcas: " + e.getMessage());
        }
    }

    /**
     * Calcula el total de horas trabajadas sumando la diferencia entre
     * cada ENTRADA y su SALIDA correspondiente (por empleado, por dia),
     * usando solo streams. Las marcas que no tienen su par (inconsistentes)
     * no se cuentan, ya que no se puede calcular una duracion real.
     */
    private double calcularTotalHorasTrabajadas(List<Marca> marcas) {
        Map<Long, List<Marca>> marcasPorEmpleado = marcas.stream()
                .collect(Collectors.groupingBy(m -> m.getEmpleado().getId()));

        return marcasPorEmpleado.values().stream()
                .flatMap(marcasDeUnEmpleado -> sumarHorasPorDia(marcasDeUnEmpleado).stream())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    // Agrupa las marcas de UN empleado por dia, y para cada dia calcula
    // las horas entre su primera entrada y su ultima salida.
    private List<Double> sumarHorasPorDia(List<Marca> marcasDeUnEmpleado) {
        Map<LocalDate, List<Marca>> porDia = marcasDeUnEmpleado.stream()
                .collect(Collectors.groupingBy(Marca::getFecha));

        return porDia.values().stream()
                .map(marcasDelDia -> {
                    LocalDateTime entrada = marcasDelDia.stream()
                            .filter(m -> "ENTRADA".equals(m.getTipo()))
                            .map(Marca::getHora)
                            .min(LocalDateTime::compareTo)
                            .orElse(null);

                    LocalDateTime salida = marcasDelDia.stream()
                            .filter(m -> "SALIDA".equals(m.getTipo()))
                            .map(Marca::getHora)
                            .max(LocalDateTime::compareTo)
                            .orElse(null);

                    if (entrada == null || salida == null) {
                        return 0.0;
                    }

                    return Duration.between(entrada, salida).toMinutes() / 60.0;
                })
                .collect(Collectors.toList());
    }

    private MarcaDto convertirADto(Marca marca) {
        MarcaDto dto = new MarcaDto();
        dto.setId(marca.getId());
        dto.setFecha(marca.getFecha());
        dto.setHora(marca.getHora());
        dto.setTipo(marca.getTipo());
        dto.setEmpleadoId(marca.getEmpleado() != null ? marca.getEmpleado().getId() : null);
        return dto;
    }
}