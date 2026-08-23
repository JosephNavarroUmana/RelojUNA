package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.modelo.DetallePlanilla;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.modelo.Marca;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Calculadora de horas y salario para la generacion de planillas (punto 6).
 *
 * Migra la logica ya verificada de PlanillaServiceSimulado.calcularHorasDelDia()
 * del cliente, adaptada a las entidades reales del servidor (Marca, Empleado,
 * DetallePlanilla) y usando streams para el procesamiento (en linea con el
 * espiritu del proyecto, aunque el punto 6 no lo exige explicitamente como el
 * punto 5 y el 7).
 *
 * Reglas (segun enunciado):
 * - Jornada diurna: 2:00am a 10:00pm. Si la marca se sale de ese rango, la
 *   jornada completa se considera nocturna.
 * - Multiplicador nocturno: 1.3333 (aplica a TODAS las horas de una jornada
 *   nocturna, tanto ordinarias como extra).
 * - Limite antes de pasar a horas extra: 8h diurna, 6h nocturna.
 * - Multiplicador de horas extra: 1.5 (se aplica DESPUES del multiplicador
 *   nocturno si corresponde).
 * - Domingo = dia libre pagado doble (se duplica el total del dia, ya con
 *   los multiplicadores nocturno/extra ya aplicados).
 * - La jornada de cada dia se redondea a bloques de 30 minutos antes de
 *   calcular cualquier multiplicador.
 */
public class CalculadoraJornada {

    // Hora limite inferior y superior de la jornada diurna
    private static final LocalTime INICIO_JORNADA_DIURNA = LocalTime.of(2, 0);
    private static final LocalTime FIN_JORNADA_DIURNA = LocalTime.of(22, 0);

    // Multiplicador para convertir horas reales a horas nocturnas
    private static final double MULTIPLICADOR_NOCTURNO = 1.3333;

    // Multiplicador para las horas extras
    private static final double MULTIPLICADOR_EXTRA = 1.5;

    /**
     * Calcula el detalle de planilla de un empleado, a partir de sus marcas
     * ya filtradas al rango de fechas del mes a generar (esto NO consulta la
     * BD, solo procesa la lista que se le entregue).
     *
     * @param empleado empleado al que pertenece el detalle
     * @param marcasDelEmpleadoEnRango marcas del empleado ya filtradas al mes
     * @return DetallePlanilla con horas y salario calculados (sin planilla
     *         asignada todavia; eso lo hace PlanillaService al persistir)
     */
    public DetallePlanilla calcularDetalle(Empleado empleado, List<Marca> marcasDelEmpleadoEnRango) {

        // Agrupamos las marcas por dia para procesar una jornada a la vez
        Map<LocalDate, List<Marca>> marcasPorDia = marcasDelEmpleadoEnRango.stream()
                .collect(Collectors.groupingBy(m -> m.getHora().toLocalDate()));

        double acumuladoOrdinarias = 0.0;
        double acumuladoExtras = 0.0;
        double acumuladoDobles = 0.0;

        for (Map.Entry<LocalDate, List<Marca>> entry : marcasPorDia.entrySet()) {
            LocalDate dia = entry.getKey();
            List<Marca> marcasDelDia = entry.getValue();

            LocalDateTime entrada = buscarPrimeraEntrada(marcasDelDia);
            LocalDateTime salida = buscarUltimaSalida(marcasDelDia);

            // Si no hay entrada y salida ese dia, no se puede calcular la jornada
            // (esto seria una inconsistencia, ya detectada aparte en el punto 5)
            if (entrada == null || salida == null) {
                continue;
            }

            double[] horasDelDia = calcularHorasDelDia(entrada, salida);
            double horasOrdinariasDelDia = horasDelDia[0];
            double horasExtrasDelDia = horasDelDia[1];

            // Los domingos son el dia libre, si se trabaja se paga doble
            boolean esDiaLibre = dia.getDayOfWeek().getValue() == 7;

            if (esDiaLibre) {
                acumuladoDobles += (horasOrdinariasDelDia + horasExtrasDelDia) * 2;
            } else {
                acumuladoOrdinarias += horasOrdinariasDelDia;
                acumuladoExtras += horasExtrasDelDia;
            }
        }

        double salarioTotal = (acumuladoOrdinarias + acumuladoExtras + acumuladoDobles) * empleado.getSalarioHora();

        DetallePlanilla detalle = new DetallePlanilla();
        detalle.setEmpleado(empleado);
        // OJO: horasOrdinarias/Extras/Dobles son Integer en la entidad, por lo
        // que aqui se redondea. Esto puede perder precision de decimales que
        // salen de multiplicar por 1.3333 (ver aviso arriba en el chat).
        detalle.setHorasOrdinarias((int) Math.round(acumuladoOrdinarias));
        detalle.setHorasExtras((int) Math.round(acumuladoExtras));
        detalle.setHorasDobles((int) Math.round(acumuladoDobles));
        detalle.setSalarioTotal(salarioTotal);

        return detalle;
    }

    // Busca la primera marca de tipo ENTRADA del dia
    private LocalDateTime buscarPrimeraEntrada(List<Marca> marcasDelDia) {
        return marcasDelDia.stream()
                .filter(m -> "ENTRADA".equals(m.getTipo()))
                .map(Marca::getHora)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    // Busca la ultima marca de tipo SALIDA del dia
    private LocalDateTime buscarUltimaSalida(List<Marca> marcasDelDia) {
        return marcasDelDia.stream()
                .filter(m -> "SALIDA".equals(m.getTipo()))
                .map(Marca::getHora)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    // Calcula las horas ordinarias y extras ya pagaderas (con multiplicadores) de una jornada
    // Retorna un arreglo de 2 posiciones: [0] ordinarias, [1] extras
    private double[] calcularHorasDelDia(LocalDateTime entrada, LocalDateTime salida) {
        long minutosTrabajados = Duration.between(entrada, salida).toMinutes();

        // Redondeamos la jornada a bloques de 30 minutos
        long minutosRedondeados = Math.round(minutosTrabajados / 30.0) * 30;
        double horasTrabajadas = minutosRedondeados / 60.0;

        // Determinamos si la jornada es nocturna, si se sale del rango de 2am a 10pm
        LocalTime horaEntrada = entrada.toLocalTime();
        LocalTime horaSalida = salida.toLocalTime();
        boolean esNocturna = horaEntrada.isBefore(INICIO_JORNADA_DIURNA) || horaSalida.isAfter(FIN_JORNADA_DIURNA);

        double limiteOrdinarias = 8.0;
        double multiplicadorJornada = 1.0;

        if (esNocturna) {
            limiteOrdinarias = 6.0;
            multiplicadorJornada = MULTIPLICADOR_NOCTURNO;
        }

        double horasOrdinariasReales = Math.min(horasTrabajadas, limiteOrdinarias);
        double horasExtrasReales = Math.max(horasTrabajadas - limiteOrdinarias, 0.0);

        double horasOrdinariasPagar = horasOrdinariasReales * multiplicadorJornada;
        double horasExtrasPagar = horasExtrasReales * multiplicadorJornada * MULTIPLICADOR_EXTRA;

        return new double[] { horasOrdinariasPagar, horasExtrasPagar };
    }
}