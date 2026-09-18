package cr.ac.una.relojservidor.servicio;

import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.dto.ListaEmpleadoDto;
import cr.ac.una.relojservidor.modelo.Empleado;
import cr.ac.una.relojservidor.util.Respuesta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EmpleadoService {
    
    @PersistenceContext(unitName = "RelojUNAPU")
    private EntityManager em;

   public Respuesta guardar(EmpleadoDto dto) {
    try {
        //--- Validaciones de negocio (defensa en profundidad: el cliente ya valida esto,
        //pero el servidor no deberia confiar ciegamente en el cliente) ---

        LocalDate fechaNac = LocalDate.parse(dto.getFechaNacimiento());
        if (!fechaNac.isBefore(LocalDate.now())) {
            return new Respuesta(false, "La fecha de nacimiento no puede ser hoy ni en el futuro.");
        }
        if (fechaNac.isAfter(LocalDate.now().minusYears(18))) {
            return new Respuesta(false, "El empleado debe ser mayor de edad (al menos 18 años).");
        }

        boolean esAdmin = dto.getEsAdmin() != null && dto.getEsAdmin() == 1;
        if (esAdmin && (dto.getClave() == null || dto.getClave().isBlank())) {
            return new Respuesta(false, "Los administradores deben tener una clave.");
        }

        boolean esNuevo = dto.getId() == null;
        Empleado empleado;

        if (esNuevo) {
            empleado = new Empleado();
            empleado.setNombre(dto.getNombre());
            empleado.setApellidos(dto.getApellidos());
            empleado.setCedula(dto.getCedula());
            empleado.setFechaNacimiento(fechaNac);
            empleado.setFoto(dto.getFoto());
            empleado.setSalarioHora(dto.getSalarioHora());
            empleado.setEsAdmin(esAdmin ? 1 : 0);
            empleado.setClave(esAdmin ? dto.getClave() : null);

            //Folio temporal unico solo para no violar la restriccion NOT NULL/UNIQUE
            //durante el instante entre el persist y el flush; se sobreescribe abajo.
            empleado.setFolio("TMP-" + System.nanoTime());

            em.persist(empleado);
            //Forzamos el INSERT ahora para que Oracle asigne el id via secuencia,
            //necesario porque el folio depende del id.
            em.flush();

            empleado.setFolio(generarFolio(empleado));
            //No hace falta merge: la entidad sigue managed, el cambio se sincroniza al hacer commit.

        } else {
            empleado = em.find(Empleado.class, dto.getId());
            if (empleado == null) {
                return new Respuesta(false, "No se encontró el empleado con ID " + dto.getId());
            }

            empleado.setNombre(dto.getNombre());
            empleado.setApellidos(dto.getApellidos());
            empleado.setCedula(dto.getCedula());
            empleado.setFechaNacimiento(fechaNac);
            empleado.setSalarioHora(dto.getSalarioHora());
            empleado.setEsAdmin(esAdmin ? 1 : 0);
            empleado.setClave(esAdmin ? dto.getClave() : null);
            //El folio NO se toca aqui: una vez generado, es inmutable.

            //La foto solo se actualiza si el cliente mando una nueva; null/vacio
            //significa "no tocar la foto existente"
            if (dto.getFoto() != null && dto.getFoto().length > 0) {
                empleado.setFoto(dto.getFoto());
            }

            em.merge(empleado);
        }

        return new Respuesta(true, "Empleado guardado con éxito", convertirADto(empleado));

    } catch (Exception e) {
        return new Respuesta(false, "Error al guardar empleado: " + e.getMessage());
    }
}

//Genera el folio visible del empleado, ej: "FJ-0007"
//F fijo + primera letra del nombre (mayuscula) + id con 4 digitos de relleno
private String generarFolio(Empleado empleado) {
    char inicial = empleado.getNombre() != null && !empleado.getNombre().isBlank()
            ? Character.toUpperCase(empleado.getNombre().charAt(0))
            : 'X';
    return "F" + inicial + "-" + String.format("%04d", empleado.getId());
}

 public Respuesta obtenerTodos() {
    try {
        List<Empleado> empleados = em.createQuery("SELECT e FROM Empleado e", Empleado.class)
                .getResultList();

        List<EmpleadoDto> dtos = empleados.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        //Metemos la lista dentro del envoltorio para que JAXB la pueda mandar
        ListaEmpleadoDto listaEnvoltorio = new ListaEmpleadoDto(dtos);

        return new Respuesta(true, "Empleados obtenidos con exito", listaEnvoltorio);

    } catch (Exception e) {
        return new Respuesta(false, "Error al obtener empleados: " + e.getMessage());
    }
}

    public Respuesta obtenerPorId(Long id) {
        try {
            Empleado empleado = em.find(Empleado.class, id);
            if (empleado == null) {
                return new Respuesta(false, "No se encontró el empleado con ID " + id);
            }
            return new Respuesta(true, "Empleado encontrado", convertirADto(empleado));

        } catch (Exception e) {
            return new Respuesta(false, "Error al buscar empleado: " + e.getMessage());
        }
    }

   public Respuesta eliminar(Long id) {
        try {
            Empleado empleado = em.find(Empleado.class, id);
            if (empleado == null) {
                return new Respuesta(false, "No se encontró el empleado con ID " + id);
            }
            Long cantidadDetalles = em.createQuery(
                    "SELECT COUNT(d) FROM DetallePlanilla d WHERE d.empleado.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (cantidadDetalles > 0) {
                return new Respuesta(false, "No se puede eliminar: el empleado tiene planillas registradas.");
            }
            em.remove(empleado);
            return new Respuesta(true, "Empleado eliminado con éxito");
        } catch (Exception e) {
            return new Respuesta(false, "Error al eliminar empleado: " + e.getMessage());
        }
    }

    // --- Método auxiliar de conversión Entidad -> DTO ---
    private EmpleadoDto convertirADto(Empleado empleado) {
        EmpleadoDto dto = new EmpleadoDto();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellidos(empleado.getApellidos());
        dto.setCedula(empleado.getCedula());
        dto.setFechaNacimiento(empleado.getFechaNacimiento().toString());
        dto.setFoto(empleado.getFoto());
        dto.setFolio(empleado.getFolio());
        dto.setSalarioHora(empleado.getSalarioHora());
        dto.setEsAdmin(empleado.getEsAdmin());
        dto.setClave(empleado.getClave());
        return dto;
    }
}