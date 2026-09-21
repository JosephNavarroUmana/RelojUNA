package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.ListaMarcaDto;
import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.modelo.Marca;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

    public Respuesta guardar(MarcaDto dto) {
        try {
            Empleado empleado = buscarEmpleadoPorFolio(dto.getFolioEmpleado());

            if (empleado == null) {
                return new Respuesta(false, "No se encontro el empleado con folio " + dto.getFolioEmpleado());
            }

            Marca marca;
            if (dto.getId() == null) {
                marca = new Marca();
            } else {
                marca = em.find(Marca.class, dto.getId());
                if (marca == null) {
                    return new Respuesta(false, "No se encontro la marca con ID " + dto.getId());
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

            return new Respuesta(true, "Marca guardada con exito", convertirADto(marca));

        } catch (Exception e) {
            return new Respuesta(false, "Error al guardar marca: " + e.getMessage());
        }
    }

    private Empleado buscarEmpleadoPorFolio(String folio) {
        try {
            return em.createQuery("SELECT e FROM Empleado e WHERE e.folio = :folio", Empleado.class)
                    .setParameter("folio", folio)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Respuesta marcar(Long empleadoId) {
        try {
            Empleado empleado = em.find(Empleado.class, empleadoId);
            if (empleado == null) {
                return new Respuesta(false, "No se encontro el empleado con ID " + empleadoId);
            }

            List<Marca> marcasDelEmpleado = em.createQuery(
                    "SELECT m FROM Marca m WHERE m.empleado.id = :empId", Marca.class)
                    .setParameter("empId", empleadoId)
                    .getResultList();

            Marca ultima = marcasDelEmpleado.stream()
                    .max(Comparator.comparing(Marca::getHora))
                    .orElse(null);

            String tipo = "ENTRADA";
            if (ultima != null && "ENTRADA".equals(ultima.getTipo())) {
                long horas = java.time.Duration.between(ultima.getHora(), java.time.LocalDateTime.now()).toHours();
                if (horas <= 16) {
                    tipo = "SALIDA";
                }
            }

            Marca marca = new Marca();
            marca.setEmpleado(empleado);
            marca.setFecha(LocalDate.now());
            marca.setHora(java.time.LocalDateTime.now());
            marca.setTipo(tipo);

            em.persist(marca);

            return new Respuesta(true, "Marca de " + tipo + " registrada con exito", convertirADto(marca));

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

            ListaMarcaDto listaEnvoltorio = new ListaMarcaDto(dtos);

            return new Respuesta(true, "Marcas obtenidas con exito", listaEnvoltorio);

        } catch (Exception e) {
            return new Respuesta(false, "Error al obtener marcas: " + e.getMessage());
        }
    }

    public Respuesta eliminar(Long id) {
        try {
            Marca marca = em.find(Marca.class, id);
            if (marca == null) {
                return new Respuesta(false, "No se encontro la marca con ID " + id);
            }

            em.remove(marca);

            return new Respuesta(true, "Marca eliminada con exito");

        } catch (Exception e) {
            return new Respuesta(false, "Error al eliminar marca: " + e.getMessage());
        }
    }

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

            ListaMarcaDto listaEnvoltorio = new ListaMarcaDto(dtos);

            return new Respuesta(true, "Inconsistencias encontradas: " + dtos.size(), listaEnvoltorio);

        } catch (Exception e) {
            return new Respuesta(false, "Error al buscar inconsistencias: " + e.getMessage());
        }
    }

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

    //Convierte la entidad a dto
    private MarcaDto convertirADto(Marca marca) {
        MarcaDto dto = new MarcaDto();
        dto.setId(marca.getId());
        dto.setFecha(marca.getFecha());
        dto.setHora(marca.getHora());
        dto.setTipo(marca.getTipo());
        dto.setEmpleadoId(marca.getEmpleado() != null ? marca.getEmpleado().getId() : null);

        if (marca.getEmpleado() != null) {
            dto.setFolioEmpleado(marca.getEmpleado().getFolio());
            dto.setNombreEmpleado(marca.getEmpleado().getNombre() + " " + marca.getEmpleado().getApellidos());
        }

        return dto;
    }
}