package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.MarcaDto;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.modelo.Marca;
import cr.ac.una.relojservidor.util.EntityManagerHelper;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

public class MarcaService {

    public Respuesta guardar(MarcaDto dto) {
        EntityManager em = EntityManagerHelper.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Marca marca;
            if (dto.getId() == null) {
                // Es una marca nueva
                marca = new Marca();
            } else {
                // Es una edición de una existente
                marca = em.find(Marca.class, dto.getId());
                if (marca == null) {
                    tx.rollback();
                    return new Respuesta(false, "No se encontró la marca con ID " + dto.getId());
                }
            }

            Empleado empleado = em.find(Empleado.class, dto.getEmpleadoId());
            if (empleado == null) {
                tx.rollback();
                return new Respuesta(false, "No se encontró el empleado con ID " + dto.getEmpleadoId());
            }

            marca.setFecha(dto.getFecha());
            marca.setHora(dto.getHora());
            marca.setTipo(dto.getTipo());
            marca.setEmpleado(empleado);

            if (dto.getId() == null) {
                em.persist(marca);
            } else {
                em.merge(marca);
            }

            tx.commit();

            return new Respuesta(true, "Marca guardada con éxito", convertirADto(marca));

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return new Respuesta(false, "Error al guardar marca: " + e.getMessage());
        }
    }

    public Respuesta obtenerTodos() {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            List<Marca> marcas = em.createQuery("SELECT m FROM Marca m", Marca.class)
                    .getResultList();

            List<MarcaDto> dtos = marcas.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());

            return new Respuesta(true, "Marcas obtenidas con éxito", dtos);

        } catch (Exception e) {
            return new Respuesta(false, "Error al obtener marcas: " + e.getMessage());
        }
    }

    public Respuesta obtenerPorId(Long id) {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            Marca marca = em.find(Marca.class, id);
            if (marca == null) {
                return new Respuesta(false, "No se encontró la marca con ID " + id);
            }
            return new Respuesta(true, "Marca encontrada", convertirADto(marca));

        } catch (Exception e) {
            return new Respuesta(false, "Error al buscar marca: " + e.getMessage());
        }
    }

    public Respuesta obtenerPorEmpleado(Long empleadoId) {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            List<Marca> marcas = em.createQuery(
                            "SELECT m FROM Marca m WHERE m.empleado.id = :empleadoId ORDER BY m.fecha, m.hora",
                            Marca.class)
                    .setParameter("empleadoId", empleadoId)
                    .getResultList();

            List<MarcaDto> dtos = marcas.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());

            return new Respuesta(true, "Marcas obtenidas con éxito", dtos);

        } catch (Exception e) {
            return new Respuesta(false, "Error al obtener marcas del empleado: " + e.getMessage());
        }
    }

    public Respuesta eliminar(Long id) {
        EntityManager em = EntityManagerHelper.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Marca marca = em.find(Marca.class, id);
            if (marca == null) {
                tx.rollback();
                return new Respuesta(false, "No se encontró la marca con ID " + id);
            }

            em.remove(marca);
            tx.commit();

            return new Respuesta(true, "Marca eliminada con éxito");

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return new Respuesta(false, "Error al eliminar marca: " + e.getMessage());
        }
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