package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.DetallePlanillaDto;
import cr.ac.una.relojservidor.dto.PlanillaDto;
import cr.ac.una.relojservidor.modelo.DetallePlanilla;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.modelo.Marca;
import cr.ac.una.relojservidor.modelo.Planilla;
import cr.ac.una.relojservidor.util.EntityManagerHelper;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlanillaService {

    // =========================================================
    // CRUD básico
    // =========================================================

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

            Respuesta errorDetalle = sincronizarDetalles(em, planilla, dto.getDetalles());
            if (errorDetalle != null) {
                tx.rollback();
                return errorDetalle;
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

    // =========================================================
    // Generación automática de planilla (punto 6 del enunciado)
    // =========================================================

    /**
     * Genera una planilla nueva para el mes/año indicado, calculando
     * automáticamente las horas de cada empleado a partir de sus marcas.
     *
     * NOTA: el cálculo de horas ordinarias/extras/dobles usado aquí es
     * un primer borrador simple. Cuando esté lista CalculadoraJornada,
     * hay que reemplazar el bloque marcado más abajo para que aplique
     * las reglas reales de diurna/nocturna/domingo/feriado.
     */
    public Respuesta generarPlanilla(int mes, int anio) {
        EntityManager em = EntityManagerHelper.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Planilla planilla = new Planilla();
            planilla.setMes(mes);
            planilla.setAnio(anio);
            planilla.setFechaGeneracion(LocalDate.now());
            planilla.setDetalles(new ArrayList<>());

            List<Empleado> empleados = em.createQuery(
                    "SELECT e FROM Empleado e", Empleado.class).getResultList();

            for (Empleado empleado : empleados) {
                List<Marca> marcasDelMes = em.createQuery(
                        "SELECT m FROM Marca m WHERE m.empleado.id = :empId "
                        + "AND FUNCTION('MONTH', m.fecha) = :mes "
                        + "AND FUNCTION('YEAR', m.fecha) = :anio",
                        Marca.class)
                        .setParameter("empId", empleado.getId())
                        .setParameter("mes", mes)
                        .setParameter("anio", anio)
                        .getResultList();

                if (marcasDelMes.isEmpty()) {
                    continue; // este empleado no marcó nada ese mes, se omite
                }

                // --- BLOQUE TEMPORAL: reemplazar por CalculadoraJornada ---
                // Por ahora solo cuenta pares ENTRADA/SALIDA como horas ordinarias,
                // sin distinguir diurna/nocturna/extra/doble todavía.
                long totalHoras = contarHorasBasico(marcasDelMes);
                int horasOrdinarias = (int) Math.min(totalHoras, 160); // tope simple de referencia
                int horasExtras = (int) Math.max(0, totalHoras - 160);
                int horasDobles = 0;
                double salarioTotal = (horasOrdinarias + horasExtras * 1.5) * empleado.getSalarioHora();
                // --- FIN BLOQUE TEMPORAL ---

                DetallePlanilla detalle = new DetallePlanilla();
                detalle.setEmpleado(empleado);
                detalle.setPlanilla(planilla);
                detalle.setHorasOrdinarias(horasOrdinarias);
                detalle.setHorasExtras(horasExtras);
                detalle.setHorasDobles(horasDobles);
                detalle.setSalarioTotal(salarioTotal);

                planilla.getDetalles().add(detalle);
            }

            if (planilla.getDetalles().isEmpty()) {
                tx.rollback();
                return new Respuesta(false, "No hay marcas registradas para " + mes + "/" + anio);
            }

            em.persist(planilla);
            tx.commit();

            return new Respuesta(true, "Planilla generada con éxito", convertirADto(planilla));

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return new Respuesta(false, "Error al generar planilla: " + e.getMessage());
        }
    }

    /**
     * Cálculo temporal y muy básico: cuenta cuántas horas pasaron entre
     * cada ENTRADA y su SALIDA correspondiente, sumando todo el mes.
     * Se debe reemplazar por la lógica real de CalculadoraJornada.
     */
    private long contarHorasBasico(List<Marca> marcas) {
        List<Marca> ordenadas = marcas.stream()
                .sorted((a, b) -> a.getHora().compareTo(b.getHora()))
                .collect(Collectors.toList());

        long totalMinutos = 0;
        Marca entradaPendiente = null;

        for (Marca m : ordenadas) {
            if ("ENTRADA".equalsIgnoreCase(m.getTipo())) {
                entradaPendiente = m;
            } else if ("SALIDA".equalsIgnoreCase(m.getTipo()) && entradaPendiente != null) {
                totalMinutos += java.time.Duration.between(
                        entradaPendiente.getHora(), m.getHora()).toMinutes();
                entradaPendiente = null;
            }
        }

        return totalMinutos / 60;
    }

    // =========================================================
    // Helpers privados
    // =========================================================

    private Respuesta sincronizarDetalles(EntityManager em, Planilla planilla, List<DetallePlanillaDto> detallesDto) {
        planilla.getDetalles().clear();

        if (detallesDto == null) {
            return null;
        }

        for (DetallePlanillaDto detDto : detallesDto) {
            Empleado empleado = em.find(Empleado.class, detDto.getEmpleadoId());
            if (empleado == null) {
                return new Respuesta(false, "No se encontró el empleado con ID " + detDto.getEmpleadoId());
            }

            DetallePlanilla detalle;
            if (detDto.getId() != null) {
                detalle = em.find(DetallePlanilla.class, detDto.getId());
                if (detalle == null) {
                    return new Respuesta(false, "No se encontró el detalle con ID " + detDto.getId());
                }
            } else {
                detalle = new DetallePlanilla();
            }

            detalle.setHorasOrdinarias(detDto.getHorasOrdinarias());
            detalle.setHorasExtras(detDto.getHorasExtras());
            detalle.setHorasDobles(detDto.getHorasDobles());
            detalle.setSalarioTotal(detDto.getSalarioTotal());
            detalle.setEmpleado(empleado);
            detalle.setPlanilla(planilla);

            planilla.getDetalles().add(detalle);
        }

        return null; // sin errores
    }

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