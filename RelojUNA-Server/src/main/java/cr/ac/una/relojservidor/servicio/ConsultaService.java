package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.ConsultaResultadoDto;
import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.modelo.Marca;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class ConsultaService {

    @PersistenceContext(unitName = "RelojUNAPU")
    private EntityManager em;

    public Respuesta consultarMarcas(LocalDate desde, LocalDate hasta, Long empleadoId) {
        try {
            //Consulta principal y unica
            List<Marca> todasLasMarcas = em.createQuery(
                    "SELECT m FROM Marca m", Marca.class).getResultList();

            //Strems
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

    private double calcularTotalHorasTrabajadas(List<Marca> marcas) {
        Map<Long, List<Marca>> marcasPorEmpleado = marcas.stream()
                .collect(Collectors.groupingBy(m -> m.getEmpleado().getId()));

        return marcasPorEmpleado.values().stream()
                .flatMap(marcasDeUnEmpleado -> sumarHorasPorDia(marcasDeUnEmpleado).stream())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    //Juntar las marcas de un empleado y calcular las horas entre la primera entrada y la ultima salida.
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