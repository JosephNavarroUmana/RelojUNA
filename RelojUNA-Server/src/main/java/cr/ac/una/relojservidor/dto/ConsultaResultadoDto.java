package cr.ac.una.relojservidor.dto;

import java.io.Serializable;
import java.util.List;

public class ConsultaResultadoDto implements Serializable {

    private List<MarcaDto> marcas;
    private long cantidadEmpleados;
    private long totalMarcas;
    private double totalHorasTrabajadas;

    public ConsultaResultadoDto() {
    }

    public ConsultaResultadoDto(List<MarcaDto> marcas, long cantidadEmpleados, long totalMarcas, double totalHorasTrabajadas) {
        this.marcas = marcas;
        this.cantidadEmpleados = cantidadEmpleados;
        this.totalMarcas = totalMarcas;
        this.totalHorasTrabajadas = totalHorasTrabajadas;
    }

    public List<MarcaDto> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<MarcaDto> marcas) {
        this.marcas = marcas;
    }

    public long getCantidadEmpleados() {
        return cantidadEmpleados;
    }

    public void setCantidadEmpleados(long cantidadEmpleados) {
        this.cantidadEmpleados = cantidadEmpleados;
    }

    public long getTotalMarcas() {
        return totalMarcas;
    }

    public void setTotalMarcas(long totalMarcas) {
        this.totalMarcas = totalMarcas;
    }

    public double getTotalHorasTrabajadas() {
        return totalHorasTrabajadas;
    }

    public void setTotalHorasTrabajadas(double totalHorasTrabajadas) {
        this.totalHorasTrabajadas = totalHorasTrabajadas;
    }
}