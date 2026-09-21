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

public class CalculadoraJornada {

   
    private static final LocalTime INICIO_JORNADA_DIURNA = LocalTime.of(2, 0);
    private static final LocalTime FIN_JORNADA_DIURNA = LocalTime.of(22, 0);
    private static final double MULTIPLICADOR_NOCTURNO = 1.3333;
    private static final double MULTIPLICADOR_EXTRA = 1.5;

    public DetallePlanilla calcularDetalle(Empleado empleado, List<Marca> marcasDelEmpleadoEnRango) {

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

              if (entrada == null || salida == null) {
                continue;
            }

            if (salida.isBefore(entrada)) {
                continue;
            }

            double[] horasDelDia = calcularHorasDelDia(entrada, salida);
            double horasOrdinariasDelDia = horasDelDia[0];
            double horasExtrasDelDia = horasDelDia[1];

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
        detalle.setHorasOrdinarias((int) Math.round(acumuladoOrdinarias));
        detalle.setHorasExtras((int) Math.round(acumuladoExtras));
        detalle.setHorasDobles((int) Math.round(acumuladoDobles));
        detalle.setSalarioTotal(salarioTotal);

        return detalle;
    }

    private LocalDateTime buscarPrimeraEntrada(List<Marca> marcasDelDia) {
        return marcasDelDia.stream()
                .filter(m -> "ENTRADA".equals(m.getTipo()))
                .map(Marca::getHora)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private LocalDateTime buscarUltimaSalida(List<Marca> marcasDelDia) {
        return marcasDelDia.stream()
                .filter(m -> "SALIDA".equals(m.getTipo()))
                .map(Marca::getHora)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    private double[] calcularHorasDelDia(LocalDateTime entrada, LocalDateTime salida) {
        long minutosTrabajados = Duration.between(entrada, salida).toMinutes();

        long minutosRedondeados = Math.round(minutosTrabajados / 30.0) * 30;
        double horasTrabajadas = minutosRedondeados / 60.0;

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