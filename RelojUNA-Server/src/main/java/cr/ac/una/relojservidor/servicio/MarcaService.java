package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.modelo.Marca;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class MarcaService {

    @PersistenceContext(unitName = "RelojUNAPU")
    private EntityManager em;

    // =========================================================
    // CRUD básico
    // =========================================================

    public Respuesta guardar(MarcaDto dto) {
        try {
            Empleado empleado = em.find(Empleado.class, dto.getEmpleadoId());
            if (empleado == null) {
                return new Respuesta(false, "No se encontró el empleado con ID " + dto.getEmpleadoId());
            }

            Marca marca;
            if (dto.getId() == null) {
                marca = new Marca();
            } else {
                marca = em.find(Marca.class, dto.getId());
                if (marca == null) {
                    return new Respuesta(false, "No se encontró la marca con ID " + dto.getId());
                }
            }

            marca.setFecha(dto.getFecha());
            marca.setHora(dto.getHora());
            marca.setTipo(dto.getTipo());
            marca.setEmpleado(empleado);

            if (dto.getId() == null) {
                em.persist(marca);
            } else {
                marca = em.merge(marca);
            }

            return new Respuesta(true, "Marca guardada con éxito", convertirADto(marca));

        } catch (Exception e) {
            return new Respuesta(false, "Error al guardar marca: " + e.getMessage());
        }
    }

    /**
     * Registra la siguiente marca de un empleado (ENTRADA o SALIDA),
     * decidiendo automáticamente el tipo según su última marca del día.
     */
    public Respuesta marcar(Long empleadoId) {
        try {
            Empleado empleado = em.find(Empleado.class, empleadoId);
            if (empleado == null) {
                return new Respuesta(false, "No se encontró el empleado con ID " + empleadoId);
            }

            List<Marca> marcasDelEmpleado = em.createQuery(
                    "SELECT m FROM Marca m WHERE m.empleado.id = :empId", Marca.class)
                    .setParameter("empId", empleadoId)
                    .getResultList();

            String tipo = marcasDelEmpleado.stream()
                    .max(Comparator.comparing(Marca::getHora))
                    .map(ultima -> "ENTRADA".equals(ultima.getTipo()) ? "SALIDA" : "ENTRADA")
                    .orElse("ENTRADA");

            Marca marca = new Marca();
            marca.setEmpleado(empleado);
            marca.setFecha(LocalDate.now());
            marca.setHora(java.time.LocalDateTime.now());
            marca.setTipo(tipo);

            em.persist(marca);

            return new Respuesta(true, "Marca de " + tipo + " registrada con éxito", convertirADto(marca));

        } catch (Exception e) {
            return new Respuesta(false, "Error al marcar: " + e.getMessage());
        }
    }

    public Respuesta obtenerTodas() {
        try {
            List<Marca> marcas = em.createQuery("SELECT m FROM Marca m", Marca.class).getResultList();

            List<MarcaDto> dtos = marcas.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());

            return new Respuesta(true, "Marcas obtenidas con éxito", dtos);

        } catch (Exception e) {
            return new Respuesta(false, "Error al obtener marcas: " + e.getMessage());
        }
    }

    public Respuesta eliminar(Long id) {
        try {
            Marca marca = em.find(Marca.class, id);
            if (marca == null) {
                return new Respuesta(false, "No se encontró la marca con ID " + id);
            }

            em.remove(marca);

            return new Respuesta(true, "Marca eliminada con éxito");

        } catch (Exception e) {
            return new Respuesta(false, "Error al eliminar marca: " + e.getMessage());
        }
    }

    // =========================================================
    // Detección de inconsistencias (punto 5 del enunciado, con streams)
    // =========================================================

    /**
     * Trae TODAS las marcas (única consulta JPQL simple, sin filtros),
     * y usa streams para agrupar por empleado y detectar inconsistencias:
     * marcas que no alternan correctamente ENTRADA/SALIDA.
     */
    public Respuesta buscarInconsistencias() {
        try {
            List<Marca> todasLasMarcas = em.createQuery(
                    "SELECT m FROM Marca m", Marca.class).getResultList();

            Map<Empleado, List<Marca>> marcasPorEmpleado = todasLasMarcas.stream()
                    .collect(Collectors.groupingBy(Marca::getEmpleado));

            List<Marca> inconsistentes = marcasPorEmpleado.values().stream()
                    .flatMap(marcasDeUnEmpleado -> detectarInconsistencias(marcasDeUnEmpleado).stream())
                    .collect(Collectors.toList());

            List<MarcaDto> dtos = inconsistentes.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());

            return new Respuesta(true, "Inconsistencias encontradas: " + dtos.size(), dtos);

        } catch (Exception e) {
            return new Respuesta(false, "Error al buscar inconsistencias: " + e.getMessage());
        }
    }

    /**
     * Recibe las marcas YA de un solo empleado, las ordena por hora,
     * y marca como inconsistente cualquier marca que no alterne
     * correctamente con la anterior (dos ENTRADA seguidas, dos SALIDA
     * seguidas, etc.).
     */
    private List<Marca> detectarInconsistencias(List<Marca> marcasDeUnEmpleado) {
        List<Marca> ordenadas = marcasDeUnEmpleado.stream()
                .sorted(Comparator.comparing(Marca::getHora))
                .collect(Collectors.toList());

        List<Marca> inconsistentes = new ArrayList<>();

        for (int i = 0; i < ordenadas.size() - 1; i++) {
            Marca actual = ordenadas.get(i);
            Marca siguiente = ordenadas.get(i + 1);

            if (actual.getTipo().equals(siguiente.getTipo())) {
                inconsistentes.add(actual);
                inconsistentes.add(siguiente);
            }
        }

        return inconsistentes.stream().distinct().collect(Collectors.toList());
    }

    // --- Método auxiliar de conversión Entidad -> DTO ---
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