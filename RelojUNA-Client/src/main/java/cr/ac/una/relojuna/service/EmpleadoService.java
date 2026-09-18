package cr.ac.una.relojuna.service;

import cr.ac.una.relojuna.model.EmpleadoDto;
import cr.ac.una.relojuna.util.Respuesta;
import cr.ac.una.relojuna.ws.EmpleadoWS;
import cr.ac.una.relojuna.ws.EmpleadoWS_Service;
import cr.ac.una.relojuna.ws.ListaEmpleadoDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

public class EmpleadoService {

    private EmpleadoWS puerto;

    public EmpleadoService() {
        EmpleadoWS_Service servicioWS = new EmpleadoWS_Service();
        puerto = servicioWS.getEmpleadoWSPort();
    }

  public Respuesta buscarEmpleados(String texto) {
    try {
        cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.obtenerEmpleados();

        if (!respuestaServidor.isExito()) {
            return new Respuesta(false, respuestaServidor.getMensaje(), "");
        }

        Object resultadoCrudo = respuestaServidor.getAny();
        ListaEmpleadoDto listaEnvoltorio = convertirAListaEmpleadoDto(resultadoCrudo);

        List<cr.ac.una.relojuna.ws.EmpleadoDto> empleadosServidor = listaEnvoltorio.getEmpleados();

        List<EmpleadoDto> resultado = new ArrayList<>();

        for (cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor : empleadosServidor) {
            EmpleadoDto empleado = convertirAEmpleadoCliente(empleadoServidor);

            if (texto == null || texto.isBlank()) {
                resultado.add(empleado);
                continue;
            }

            String textoBusqueda = texto.toLowerCase();
            String nombreCompleto = empleado.getNombre().toLowerCase() + " " + empleado.getApellidos().toLowerCase();
            String folioTexto = empleado.getFolio() != null ? empleado.getFolio().toLowerCase() : "";

            if (nombreCompleto.contains(textoBusqueda) || folioTexto.contains(textoBusqueda)) {
                resultado.add(empleado);
            }
        }

        return new Respuesta(true, "", "", "Empleados", resultado);
    } catch (Exception ex) {
        ex.printStackTrace();
        return new Respuesta(false, "Error buscando los empleados.", "buscarEmpleados " + ex.getMessage());
    }
}


 //Convierte el resultado crudo que manda el servidor al envoltorio de la lista de empleados
//Puede llegar como JAXBElement, como el tipo directo, o como un nodo XML sin procesar
private ListaEmpleadoDto convertirAListaEmpleadoDto(Object resultadoCrudo) throws Exception {
    if (resultadoCrudo instanceof JAXBElement) {
        return (ListaEmpleadoDto) ((JAXBElement<?>) resultadoCrudo).getValue();
    }

    if (resultadoCrudo instanceof ListaEmpleadoDto) {
        return (ListaEmpleadoDto) resultadoCrudo;
    }

    //Si llega como nodo XML crudo, lo desempacamos a mano con un Unmarshaller
    //Usamos la variante que recibe la clase esperada, asi no importa el nombre del elemento raiz
    if (resultadoCrudo instanceof Element) {
        JAXBContext contexto = JAXBContext.newInstance(ListaEmpleadoDto.class);
        Unmarshaller desempacador = contexto.createUnmarshaller();
        JAXBElement<ListaEmpleadoDto> elemento = desempacador.unmarshal((Element) resultadoCrudo, ListaEmpleadoDto.class);
        return elemento.getValue();
    }

    throw new Exception("No se pudo interpretar el resultado del servidor");
}

  //Guarda un empleado nuevo o actualiza uno existente en el servidor
public Respuesta guardarEmpleado(EmpleadoDto empleado) {
    try {
        cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor = convertirAEmpleadoServidor(empleado);

        cr.ac.una.relojuna.ws.Respuesta respuestaServidor = puerto.guardarEmpleado(empleadoServidor);

        if (!respuestaServidor.isExito()) {
            return new Respuesta(false, respuestaServidor.getMensaje(), "");
        }

        Object resultadoCrudo = respuestaServidor.getAny();
        cr.ac.una.relojuna.ws.EmpleadoDto empleadoGuardado = convertirAEmpleadoDtoServidor(resultadoCrudo);

        EmpleadoDto empleadoConvertido = convertirAEmpleadoCliente(empleadoGuardado);

        return new Respuesta(true, "", "", "Empleado", empleadoConvertido);
    } catch (Exception ex) {
        ex.printStackTrace();
        return new Respuesta(false, "Error guardando el empleado.", "guardarEmpleado " + ex.getMessage());
    }
}

//Convierte el resultado crudo que manda el servidor a un EmpleadoDto
//Puede llegar como JAXBElement, como el tipo directo, o como un nodo XML sin procesar
private cr.ac.una.relojuna.ws.EmpleadoDto convertirAEmpleadoDtoServidor(Object resultadoCrudo) throws Exception {
    if (resultadoCrudo instanceof JAXBElement) {
        return (cr.ac.una.relojuna.ws.EmpleadoDto) ((JAXBElement<?>) resultadoCrudo).getValue();
    }

    if (resultadoCrudo instanceof cr.ac.una.relojuna.ws.EmpleadoDto) {
        return (cr.ac.una.relojuna.ws.EmpleadoDto) resultadoCrudo;
    }

    //Si llega como nodo XML crudo, lo desempacamos a mano con un Unmarshaller
    //Usamos la variante que recibe la clase esperada, asi no importa el nombre del elemento raiz
    if (resultadoCrudo instanceof Element) {
        JAXBContext contexto = JAXBContext.newInstance(cr.ac.una.relojuna.ws.EmpleadoDto.class);
        Unmarshaller desempacador = contexto.createUnmarshaller();
        JAXBElement<cr.ac.una.relojuna.ws.EmpleadoDto> elemento = desempacador.unmarshal((Element) resultadoCrudo, cr.ac.una.relojuna.ws.EmpleadoDto.class);
        return elemento.getValue();
    }

    throw new Exception("No se pudo interpretar el resultado del servidor");
}
   //Elimina un empleado del servidor usando el id interno
public Respuesta eliminarEmpleado(Long id) {
    try {
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

    empleado.setId(empleadoServidor.getId());
    empleado.setFolio(empleadoServidor.getFolio());

    empleado.setNombre(empleadoServidor.getNombre());
    empleado.setApellidos(empleadoServidor.getApellidos());
    empleado.setCedula(empleadoServidor.getCedula());

    LocalDate fechaNacimiento = null;
    if (empleadoServidor.getFechaNacimiento() != null) {
        fechaNacimiento = LocalDate.parse(empleadoServidor.getFechaNacimiento());
    }
    empleado.setFechaNacimiento(fechaNacimiento);

    empleado.setSalarioPorHora(empleadoServidor.getSalarioHora());
    empleado.setFoto(empleadoServidor.getFoto());
    empleado.setClave(empleadoServidor.getClave());

    boolean administrador = empleadoServidor.getEsAdmin() != null && empleadoServidor.getEsAdmin() == 1;
    empleado.setAdministrador(administrador);

    return empleado;
}

  private cr.ac.una.relojuna.ws.EmpleadoDto convertirAEmpleadoServidor(EmpleadoDto empleado) {
    cr.ac.una.relojuna.ws.EmpleadoDto empleadoServidor = new cr.ac.una.relojuna.ws.EmpleadoDto();

    //El id viaja tal cual; null significa empleado nuevo.
    //El folio NUNCA se manda: lo genera el servidor al crear, y no se puede editar despues.
    empleadoServidor.setId(empleado.getId());

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

    //Solo se manda foto si el usuario selecciono una nueva (foto != null);
    //null significa "no tocar la foto que ya tiene guardada"
    empleadoServidor.setFoto(empleado.getFoto());

    Integer esAdmin = (empleado.isAdministrador() != null && empleado.isAdministrador()) ? 1 : 0;
    empleadoServidor.setEsAdmin(esAdmin);

    return empleadoServidor;
}
}