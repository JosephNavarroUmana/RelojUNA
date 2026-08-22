package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.DetallePlanillaDto;
import cr.ac.una.relojservidor.dto.PlanillaDto;
import cr.ac.una.relojservidor.modelo.DetallePlanilla;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.modelo.Planilla;
import cr.ac.una.relojservidor.util.EntityManagerHelper;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlanillaService {

    public Respuesta guardar(PlanillaDto dto) {
        EntityManager em = EntityManagerHelper.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Planilla planilla;
            if (dto.getId() == null) {
                planilla = new Planilla();
                planilla.setDetalles(new ArrayList<>());
            } else {
                planilla = em.find(Planilla.class, dto.getId());
                if (planilla == null) {
                    tx.rollback();
                    return new Respuesta(false, "No se encontró la planilla con ID " + dto.getId());
                }
                if (planilla.getDetalles() == null) {
                    planilla.setDetalles(new ArrayList<>());
                }
            }

            planilla.setMes(dto.getMes());
            planilla.setAnio(dto.getAnio());
            planilla.setFechaGeneracion(dto.getFechaGeneracion());

            // Sincroniza los detalles (gracias al cascade + orphanRemoval en Planilla)
            planilla.getDetalles().clear();
            if (dto.getDetalles() != null) {
                for (DetallePlanillaDto detDto : dto.getDetalles()) {
                    Empleado empleado = em.find(Empleado.class, detDto.getEmpleadoId());
                    if (empleado == null) {
                        tx.rollback();
                        return new Respuesta(false, "No se encontró el empleado con ID " + detDto.getEmpleadoId());
                    }

                    DetallePlanilla detalle = (detDto.getId() != null)
                            ? em.find(DetallePlanilla.class, detDto.getId())
                            : new DetallePlanilla();

                    detalle.setHorasOrdinarias(detDto.getHorasOrdinarias());
                    detalle.setHorasExtras(detDto.getHorasExtras());
                    detalle.setHorasDobles(detDto.getHorasDobles());
                    detalle.setSalarioTotal(detDto.getSalarioTotal());
                    detalle.setEmpleado(empleado);
                    detalle.setPlanilla(planilla);

                    planilla.getDetalles().add(detalle);
                }
            }

            if (dto.getId() == null) {
                em.persist(planilla);
            } else {
                planilla = em.merge(planilla);
            }

            tx.commit();

            return new Respuesta(true, "Planilla guardada con éxito", convertirADto(planilla));

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return new Respuesta(false, "Error al guardar planilla: " + e.getMessage());
        }
    }

    public Respuesta obtenerTodos() {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            List<Planilla> planillas = em.createQuery(
                    "SELECT p FROM Planilla p", Planilla.class).getResultList();

            List<PlanillaDto> dtos = planillas.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());

            return new Respuesta(true, "Planillas obtenidas con éxito", dtos);

        } catch (Exception e) {
            return new Respuesta(false, "Error al obtener planillas: " + e.getMessage());
        }
    }

    public Respuesta obtenerPorId(Long id) {
        EntityManager em = EntityManagerHelper.getManager();
        try {
            Planilla planilla = em.find(Planilla.class, id);
            if (planilla == null) {
                return new Respuesta(false, "No se encontró la planilla con ID " + id);
            }
            return new Respuesta(true, "Planilla encontrada", convertirADto(planilla));

        } catch (Exception e) {
            return new Respuesta(false, "Error al buscar planilla: " + e.getMessage());
        }
    }

    public Respuesta eliminar(Long id) {
        EntityManager em = EntityManagerHelper.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Planilla planilla = em.find(Planilla.class, id);
            if (planilla == null) {
                tx.rollback();
                return new Respuesta(false, "No se encontró la planilla con ID " + id);
            }

            em.remove(planilla); // el orphanRemoval/cascade limpia los detalles
            tx.commit();

            return new Respuesta(true, "Planilla eliminada con éxito");

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return new Respuesta(false, "Error al eliminar planilla: " + e.getMessage());
        }
    }

    // --- Métodos auxiliares de conversión Entidad -> DTO ---
    private PlanillaDto convertirADto(Planilla planilla) {
        PlanillaDto dto = new PlanillaDto();
        dto.setId(planilla.getId());
        dto.setMes(planilla.getMes());
        dto.setAnio(planilla.getAnio());
        dto.setFechaGeneracion(planilla.getFechaGeneracion());

        if (planilla.getDetalles() != null) {
            dto.setDetalles(planilla.getDetalles().stream()
                    .map(this::convertirDetalleADto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private DetallePlanillaDto convertirDetalleADto(DetallePlanilla detalle) {
        DetallePlanillaDto dto = new DetallePlanillaDto();
        dto.setId(detalle.getId());
        dto.setHorasOrdinarias(detalle.getHorasOrdinarias());
        dto.setHorasExtras(detalle.getHorasExtras());
        dto.setHorasDobles(detalle.getHorasDobles());
        dto.setSalarioTotal(detalle.getSalarioTotal());
        dto.setPlanillaId(detalle.getPlanilla() != null ? detalle.getPlanilla().getId() : null);
        dto.setEmpleadoId(detalle.getEmpleado() != null ? detalle.getEmpleado().getId() : null);
        return dto;
    }
}