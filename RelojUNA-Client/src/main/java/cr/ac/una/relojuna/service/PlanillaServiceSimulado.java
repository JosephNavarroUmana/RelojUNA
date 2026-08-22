package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.model.MarcaDto;
import cr.ac.una.relojuna.model.PlanillaDto;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanillaServiceSimulado implements IPlanillaService {

    // Hora limite inferior y superior de la jornada diurna
    private static final LocalTime INICIO_JORNADA_DIURNA = LocalTime.of(2, 0);
    private static final LocalTime FIN_JORNADA_DIURNA = LocalTime.of(22, 0);

    // Multiplicador para convertir horas reales a horas nocturnas
    private static final double MULTIPLICADOR_NOCTURNO = 1.3333;

    // Multiplicador para las horas extras
    private static final double MULTIPLICADOR_EXTRA = 1.5;

    @Override
    public List<PlanillaDto> generarPlanilla(int anio, int mes) {
        List<PlanillaDto> resultado = new ArrayList<>();

        IEmpleadoService empleadoService = new EmpleadoServiceSimulado();
        List<EmpleadoDto> empleados = empleadoService.buscarEmpleados("");

        LocalDate primerDia = LocalDate.of(anio, mes, 1);
        LocalDate ultimoDia = primerDia.withDayOfMonth(primerDia.lengthOfMonth());

        for (EmpleadoDto empleado : empleados) {
            PlanillaDto planilla = calcularPlanillaDelEmpleado(empleado, primerDia, ultimoDia);
            resultado.add(planilla);
        }

        return resultado;
    }

    // Calcula la planilla de un empleado para el rango de fechas del mes indicado
    private PlanillaDto calcularPlanillaDelEmpleado(EmpleadoDto empleado, LocalDate primerDia, LocalDate ultimoDia) {
        List<MarcaDto> marcasDelEmpleado = obtenerMarcasDelEmpleadoEnRango(empleado.getFolio(), primerDia, ultimoDia);

        // Agrupamos las marcas por dia para procesar una jornada a la vez
        Map<LocalDate, List<MarcaDto>> marcasPorDia = agruparMarcasPorDia(marcasDelEmpleado);

        double acumuladoOrdinarias = 0.0;
        double acumuladoExtras = 0.0;
        double acumuladoDobles = 0.0;

        for (LocalDate dia : marcasPorDia.keySet()) {
            List<MarcaDto> marcasDelDia = marcasPorDia.get(dia);

            LocalDateTime entrada = buscarPrimeraEntrada(marcasDelDia);
            LocalDateTime salida = buscarUltimaSalida(marcasDelDia);

            // Si no hay entrada y salida ese dia, no se puede calcular la jornada
            if (entrada == null || salida == null) {
                continue;
            }

            double[] horasDelDia = calcularHorasDelDia(entrada, salida);
            double horasOrdinariasDelDia = horasDelDia[0];
            double horasExtrasDelDia = horasDelDia[1];

            // Los domingos son el dia libre, si se trabaja se paga doble
            boolean esDiaLibre = dia.getDayOfWeek().getValue() == 7;

            if (esDiaLibre) {
                acumuladoDobles = acumuladoDobles + (horasOrdinariasDelDia + horasExtrasDelDia) * 2;
            } else {
                acumuladoOrdinarias = acumuladoOrdinarias + horasOrdinariasDelDia;
                acumuladoExtras = acumuladoExtras + horasExtrasDelDia;
            }
        }

        double salarioMensual = (acumuladoOrdinarias + acumuladoExtras + acumuladoDobles) * empleado.getSalarioPorHora();

        PlanillaDto planilla = new PlanillaDto();
        planilla.setFolioEmpleado(empleado.getFolio());
        planilla.setNombreEmpleado(empleado.getNombre() + " " + empleado.getApellidos());
        planilla.setHorasOrdinarias(acumuladoOrdinarias);
        planilla.setHorasExtras(acumuladoExtras);
        planilla.setHorasDobles(acumuladoDobles);
        planilla.setSalarioMensual(salarioMensual);

        return planilla;
    }

    // Trae las marcas de un empleado especifico dentro de un rango de fechas
    private List<MarcaDto> obtenerMarcasDelEmpleadoEnRango(Integer folio, LocalDate desde, LocalDate hasta) {
        List<MarcaDto> resultado = new ArrayList<>();
        List<MarcaDto> todasLasMarcas = MarcaServiceSimulado.obtenerTodasLasMarcas();

        for (MarcaDto marca : todasLasMarcas) {
            if (!marca.getFolioEmpleado().equals(folio)) {
                continue;
            }

            LocalDate fechaMarca = marca.getFechaHora().toLocalDate();
            boolean despuesDeDesde = fechaMarca.isEqual(desde) || fechaMarca.isAfter(desde);
            boolean antesDeHasta = fechaMarca.isEqual(hasta) || fechaMarca.isBefore(hasta);

            if (despuesDeDesde && antesDeHasta) {
                resultado.add(marca);
            }
        }

        return resultado;
    }

    // Agrupa una lista de marcas segun el dia en el que ocurrieron
    private Map<LocalDate, List<MarcaDto>> agruparMarcasPorDia(List<MarcaDto> marcas) {
        Map<LocalDate, List<MarcaDto>> marcasPorDia = new HashMap<>();

        for (MarcaDto marca : marcas) {
            LocalDate dia = marca.getFechaHora().toLocalDate();
            List<MarcaDto> listaDelDia = marcasPorDia.get(dia);

            if (listaDelDia == null) {
                listaDelDia = new ArrayList<>();
                marcasPorDia.put(dia, listaDelDia);
            }

            listaDelDia.add(marca);
        }

        return marcasPorDia;
    }

    // Busca la primera marca de tipo ENTRADA del dia
    private LocalDateTime buscarPrimeraEntrada(List<MarcaDto> marcasDelDia) {
        LocalDateTime primeraEntrada = null;

        for (MarcaDto marca : marcasDelDia) {
            if (marca.getTipo().equals("ENTRADA")) {
                if (primeraEntrada == null || marca.getFechaHora().isBefore(primeraEntrada)) {
                    primeraEntrada = marca.getFechaHora();
                }
            }
        }

        return primeraEntrada;
    }

    // Busca la ultima marca de tipo SALIDA del dia
    private LocalDateTime buscarUltimaSalida(List<MarcaDto> marcasDelDia) {
        LocalDateTime ultimaSalida = null;

        for (MarcaDto marca : marcasDelDia) {
            if (marca.getTipo().equals("SALIDA")) {
                if (ultimaSalida == null || marca.getFechaHora().isAfter(ultimaSalida)) {
                    ultimaSalida = marca.getFechaHora();
                }
            }
        }

        return ultimaSalida;
    }

    // Calcula las horas ordinarias y extras ya pagaderas (con multiplicadores) de una jornada
    // Retorna un arreglo de 2 posiciones, la 0 son las ordinarias y la 1 las extras
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

        double[] resultado = new double[2];
        resultado[0] = horasOrdinariasPagar;
        resultado[1] = horasExtrasPagar;
        return resultado;
    }
}