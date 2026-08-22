package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoServiceSimulado implements IEmpleadoService {

    //Lista de empleados en memoria, simula la base de datos
    private static List<EmpleadoDto> empleados = new ArrayList<>();

    //Contador para generar el siguiente folio disponible
    private static int siguienteFolio = 4;

    //Cargamos algunos empleados de prueba al iniciar la aplicacion
    static {
        EmpleadoDto emp1 = new EmpleadoDto();
        emp1.setFolio(1);
        emp1.setNombre("Juan");
        emp1.setApellidos("Perez Gomez");
        emp1.setCedula("1-1111-1111");
        emp1.setFechaNacimiento(LocalDate.of(1990, 5, 10));
        emp1.setSalarioPorHora(5000.0);
        emp1.setFoto("");
        emp1.setClave("1234");
        emp1.setAdministrador(true);
        empleados.add(emp1);

        EmpleadoDto emp2 = new EmpleadoDto();
        emp2.setFolio(2);
        emp2.setNombre("Maria");
        emp2.setApellidos("Rodriguez Vargas");
        emp2.setCedula("2-2222-2222");
        emp2.setFechaNacimiento(LocalDate.of(1995, 8, 21));
        emp2.setSalarioPorHora(3000.0);
        emp2.setFoto("");
        emp2.setClave("1111");
        emp2.setAdministrador(false);
        empleados.add(emp2);

        EmpleadoDto emp3 = new EmpleadoDto();
        emp3.setFolio(3);
        emp3.setNombre("Carlos");
        emp3.setApellidos("Jimenez Solano");
        emp3.setCedula("3-3333-3333");
        emp3.setFechaNacimiento(LocalDate.of(1988, 1, 15));
        emp3.setSalarioPorHora(3500.0);
        emp3.setFoto("");
        emp3.setClave("2222");
        emp3.setAdministrador(false);
        empleados.add(emp3);
    }

    @Override
    public List<EmpleadoDto> buscarEmpleados(String texto) {
        List<EmpleadoDto> resultado = new ArrayList<>();

        //Si el texto viene vacio, retornamos todos los empleados
        if (texto == null || texto.isBlank()) {
            resultado.addAll(empleados);
            return resultado;
        }

        //Buscamos por nombre, apellidos o folio que contengan el texto
        String textoBusqueda = texto.toLowerCase();
        for (EmpleadoDto empleado : empleados) {
            String nombreCompleto = empleado.getNombre().toLowerCase() + " " + empleado.getApellidos().toLowerCase();
            String folioTexto = empleado.getFolio().toString();

            if (nombreCompleto.contains(textoBusqueda) || folioTexto.contains(textoBusqueda)) {
                resultado.add(empleado);
            }
        }

        return resultado;
    }

    @Override
    public EmpleadoDto guardarEmpleado(EmpleadoDto empleado) {
        //Si no tiene folio, es un empleado nuevo
        if (empleado.getFolio() == null) {
            empleado.setFolio(siguienteFolio);
            siguienteFolio = siguienteFolio + 1;
            empleados.add(empleado);
            return empleado;
        }

        //Si ya tiene folio, buscamos el empleado existente y lo actualizamos
        for (int i = 0; i < empleados.size(); i++) {
            EmpleadoDto actual = empleados.get(i);
            if (actual.getFolio().equals(empleado.getFolio())) {
                empleados.set(i, empleado);
                return empleado;
            }
        }

        //Si no lo encontro, lo agregamos igual
        empleados.add(empleado);
        return empleado;
    }

    @Override
    public void eliminarEmpleado(Integer folio) {
        EmpleadoDto empleadoAEliminar = null;

        for (EmpleadoDto empleado : empleados) {
            if (empleado.getFolio().equals(folio)) {
                empleadoAEliminar = empleado;
                break;
            }
        }

        if (empleadoAEliminar != null) {
            empleados.remove(empleadoAEliminar);
        }
    }
}