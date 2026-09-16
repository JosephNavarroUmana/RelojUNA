package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.util.Respuesta;
import cr.ac.una.relojuna.ws.EmpleadoWS;
import cr.ac.una.relojuna.ws.EmpleadoWS_Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoService {

    //Puerto que se usa para llamar los metodos del servidor por SOAP
    private EmpleadoWS puerto;

    public EmpleadoService() {
        EmpleadoWS_Service servicioWS = new EmpleadoWS_Service();
        puerto = servicioWS.getEmpleadoWSPort();
    }

    //Trae todos los empleados del servidor y filtra en el cliente por nombre, apellidos o folio
    public Respuesta buscarEmpleados(String texto) {
        try {
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.obtenerEmpleados();

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            List<cr.ac.una.relojuna.ws.EmpleadoDto> empleadosServidor = (List<cr.ac.una.relojuna.ws.EmpleadoDto>) respuestaServidor.getAny();

            List<EmpleadoDto> resultado = new ArrayList<>();

            for (cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor : empleadosServidor) {
                EmpleadoDto empleado = convertirAEmpleadoCliente(empleadoServidor);

                if (texto == null || texto.isBlank()) {
                    resultado.add(empleado);
                    continue;
                }

                String textoBusqueda = texto.toLowerCase();
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

    //Guarda un empleado nuevo o actualiza uno existente en el servidor
    public Respuesta guardarEmpleado(EmpleadoDto empleado) {
        try {
            cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor = convertirAEmpleadoServidor(empleado);

            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.guardarEmpleado(empleadoServidor);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            cr.ac.una.relojuna.ws.EmpleadoDto empleadoGuardado = (cr.ac.una.relojuna.ws.EmpleadoDto) respuestaServidor.getAny();
            EmpleadoDto empleadoConvertido = convertirAEmpleadoCliente(empleadoGuardado);

            return new Respuesta(true, "", "", "Empleado", empleadoConvertido);
        } catch (Exception ex) {
            return new Respuesta(false, "Error guardando el empleado.", "guardarEmpleado " + ex.getMessage());
        }
    }

    //Elimina un empleado del servidor, el folio del cliente es el id real en el servidor
    public Respuesta eliminarEmpleado(Integer folio) {
        try {
            Long id = Long.valueOf(folio);
            cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.eliminarEmpleado(id);

            if (!respuestaServidor.isExito()) {
                return new Respuesta(false, respuestaServidor.getMensaje(), "");
            }

            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            return new Respuesta(false, "Error eliminando el empleado.", "eliminarEmpleado " + ex.getMessage());
        }
    }

    //Convierte el empleado que viene del servidor al formato que usa el cliente
    //Se dejo publico porque LoginService tambien lo necesita
    public EmpleadoDto convertirAEmpleadoCliente(cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor) {
        EmpleadoDto empleado = new EmpleadoDto();

        Integer folio = null;
        if (empleadoServidor.getId() != null) {
            folio = empleadoServidor.getId().intValue();
        }
        empleado.setFolio(folio);

        empleado.setNombre(empleadoServidor.getNombre());
        empleado.setApellidos(empleadoServidor.getApellidos());
        empleado.setCedula(empleadoServidor.getCedula());

        LocalDate fechaNacimiento = null;
        if (empleadoServidor.getFechaNacimiento() != null) {
            fechaNacimiento = LocalDate.parse(empleadoServidor.getFechaNacimiento());
        }
        empleado.setFechaNacimiento(fechaNacimiento);

        empleado.setSalarioPorHora(empleadoServidor.getSalarioHora());

        //La foto en el servidor es un arreglo de bytes y en el cliente es texto, queda pendiente esa conversion
        empleado.setFoto("");

        empleado.setClave(empleadoServidor.getClave());

        boolean administrador = false;
        if (empleadoServidor.getEsAdmin() != null && empleadoServidor.getEsAdmin() == 1) {
            administrador = true;
        }
        empleado.setAdministrador(administrador);

        return empleado;
    }

    //Convierte el empleado del cliente al formato que espera el servidor
    private cr.ac.una.relojuna.ws.EmpleadoDto convertirAEmpleadoServidor(EmpleadoDto empleado) {
        cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor = new cr.ac.una.relojuna.ws.EmpleadoDto();

        if (empleado.getFolio() != null) {
            Long id = Long.valueOf(empleado.getFolio());
            empleadoServidor.setId(id);
            empleadoServidor.setFolio(id.toString());
        } else {
            //Es un empleado nuevo, todavia no tiene id
            //El folio de texto del servidor no puede ir vacio ni repetido, se usa un valor temporal unico
            empleadoServidor.setFolio(String.valueOf(System.currentTimeMillis()));
        }

        empleadoServidor.setNombre(empleado.getNombre());
        empleadoServidor.setApellidos(empleado.getApellidos());
        empleadoServidor.setCedula(empleado.getCedula());

        String fechaTexto = null;
        if (empleado.getFechaNacimiento() != null) {
            fechaTexto = empleado.getFechaNacimiento().toString();
        }
        empleadoServidor.setFechaNacimiento(fechaTexto);

        empleadoServidor.setSalarioHora(empleado.getSalarioPorHora());
        empleadoServidor.setClave(empleado.getClave());

        //La foto del cliente es texto y el servidor espera bytes, queda pendiente esa conversion
        empleadoServidor.setFoto(new byte[0]);

        Integer esAdmin = 0;
        if (empleado.isAdministrador() != null && empleado.isAdministrador()) {
            esAdmin = 1;
        }
        empleadoServidor.setEsAdmin(esAdmin);

        return empleadoServidor;
    }
}