package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.util.Respuesta;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoService {

    //Lista de empleados en memoria, simula la base de datos
    //Cuando este listo el Web Service SOAP se reemplaza el contenido de cada metodo por la llamada real
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

    //Busca empleados por nombre, apellidos o folio, si el texto viene vacio trae todos
    public Respuesta buscarEmpleados(String texto) {
        try {
            List<EmpleadoDto> resultado = new ArrayList<>();

            if (texto == null || texto.isBlank()) {
                resultado.addAll(empleados);
                return new Respuesta(true, "", "", "Empleados", resultado);
            }

            String textoBusqueda = texto.toLowerCase();
            for (EmpleadoDto empleado : empleados) {
                String nombreCompleto = empleado.getNombre().toLowerCase() + " " + empleado.getApellidos().toLowerCase();
                String folioTexto = empleado.getFolio().toString();

                if (nombreCompleto.contains(textoBusqueda) || folioTexto.contains(textoBusqueda)) {
                    resultado.add(empleado);
                }
            }

            return new Respuesta(true, "", "", "Empleados", resultado);
        } catch (Exception ex) {
            return new Respuesta(false, "Error buscando los empleados.", "buscarEmpleados " + ex.getMessage());
        }
    }

    //Guarda un empleado nuevo o actualiza uno existente segun el folio
    public Respuesta guardarEmpleado(EmpleadoDto empleado) {
        try {
            if (empleado.getFolio() == null) {
                empleado.setFolio(siguienteFolio);
                siguienteFolio = siguienteFolio + 1;
                empleados.add(empleado);
                return new Respuesta(true, "", "", "Empleado", empleado);
            }

            for (int i = 0; i < empleados.size(); i++) {
                EmpleadoDto actual = empleados.get(i);
                if (actual.getFolio().equals(empleado.getFolio())) {
                    empleados.set(i, empleado);
                    return new Respuesta(true, "", "", "Empleado", empleado);
                }
            }

            empleados.add(empleado);
            return new Respuesta(true, "", "", "Empleado", empleado);
        } catch (Exception ex) {
            return new Respuesta(false, "Error guardando el empleado.", "guardarEmpleado " + ex.getMessage());
        }
    }

    //Elimina un empleado segun su folio
    public Respuesta eliminarEmpleado(Integer folio) {
        try {
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

            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            return new Respuesta(false, "Error eliminando el empleado.", "eliminarEmpleado " + ex.getMessage());
        }
    }
    
    //Valida el folio y la clave, si son correctos retorna el empleado dentro de la respuesta
public Respuesta validarLogin(String folio, String clave) {
    try {
        for (EmpleadoDto empleado : empleados) {
            String folioEmpleado = empleado.getFolio().toString();

            if (folioEmpleado.equals(folio) && empleado.getClave().equals(clave)) {
                return new Respuesta(true, "", "", "Usuario", empleado);
            }
        }

        return new Respuesta(false, "Folio o clave incorrectos.", "");
    } catch (Exception ex) {
        return new Respuesta(false, "Error validando el ingreso.", "validarLogin " + ex.getMessage());
    }
}
}

