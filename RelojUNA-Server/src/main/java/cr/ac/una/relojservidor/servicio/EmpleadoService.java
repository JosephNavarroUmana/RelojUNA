package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.util.EntityManagerHelper;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

public class EmpleadoService {

    public Respuesta guardar(EmpleadoDto dto) {
        EntityManager em = EntityManagerHelper.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Empleado empleado;
            if (dto.getId() == null) {
                // Es un empleado nuevo
                empleado = new Empleado();
            } else {
                // Es una edición de uno existente
                empleado = em.find(Empleado.class, dto.getId());
                if (empleado == null) {
                    tx.rollback();
                    return new Respuesta(false, "No se encontró el empleado con ID " + dto.getId());
                }
            }

            empleado.setNombre(dto.getNombre());
            empleado.setApellidos(dto.getApellidos());
            empleado.setCedula(dto.getCedula());
            empleado.setFechaNacimiento(dto.getFechaNacimiento());
            empleado.setFoto(dto.getFoto());
            empleado.setFolio(dto.getFolio());
            empleado.setSalarioHora(dto.getSalarioHora());
            empleado.setEsAdmin(dto.getEsAdmin());
            empleado.setClave(dto.getClave());

            if (dto.getId() == null) {
                em.persist(empleado);
            } else {
                em.merge(empleado);
            }

            tx.commit();

            return new Respuesta(true, "Empleado guardado con éxito", convertirADto(empleado));

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return new Respuesta(false, "Error al guardar empleado: " + e.getMessage());
        }
    }

    public Respuesta obtenerTodos() {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            List<Empleado> empleados = em.createQuery("SELECT e FROM Empleado e", Empleado.class)
                    .getResultList();

            List<EmpleadoDto> dtos = empleados.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());

            return new Respuesta(true, "Empleados obtenidos con éxito", dtos);

        } catch (Exception e) {
            return new Respuesta(false, "Error al obtener empleados: " + e.getMessage());
        }
    }

    public Respuesta obtenerPorId(Long id) {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            Empleado empleado = em.find(Empleado.class, id);
            if (empleado == null) {
                return new Respuesta(false, "No se encontró el empleado con ID " + id);
            }
            return new Respuesta(true, "Empleado encontrado", convertirADto(empleado));

        } catch (Exception e) {
            return new Respuesta(false, "Error al buscar empleado: " + e.getMessage());
        }
    }

    public Respuesta eliminar(Long id) {
        EntityManager em = EntityManagerHelper.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Empleado empleado = em.find(Empleado.class, id);
            if (empleado == null) {
                tx.rollback();
                return new Respuesta(false, "No se encontró el empleado con ID " + id);
            }

            em.remove(empleado);
            tx.commit();

            return new Respuesta(true, "Empleado eliminado con éxito");

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return new Respuesta(false, "Error al eliminar empleado: " + e.getMessage());
        }
    }

    // --- Método auxiliar de conversión Entidad -> DTO ---
    private EmpleadoDto convertirADto(Empleado empleado) {
        EmpleadoDto dto = new EmpleadoDto();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellidos(empleado.getApellidos());
        dto.setCedula(empleado.getCedula());
        dto.setFechaNacimiento(empleado.getFechaNacimiento());
        dto.setFoto(empleado.getFoto());
        dto.setFolio(empleado.getFolio());
        dto.setSalarioHora(empleado.getSalarioHora());
        dto.setEsAdmin(empleado.getEsAdmin());
        dto.setClave(empleado.getClave());
        return dto;
    }
}