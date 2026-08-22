package cr.ac.una.relojservidor;

import cr.ac.una.relojservidor.dto.EmpleadoDto;
import cr.ac.una.relojservidor.servicio.EmpleadoService;
import cr.ac.una.relojservidor.util.Respuesta;
import java.time.LocalDate;

public class TestConexion {
    public static void main(String[] args) {
        EmpleadoService service = new EmpleadoService();

        EmpleadoDto claude = new EmpleadoDto();
        claude.setNombre("Claude");
        claude.setApellidos("Shannon Anthropic");
        claude.setCedula("000000001");
        claude.setFechaNacimiento(LocalDate.of(2024, 3, 4));
        claude.setFolio("EMP003");
        claude.setSalarioHora(0.0); // trabajo gratis, soy IA
        claude.setEsAdmin(0);

        Respuesta rClaude = service.guardar(claude);
        System.out.println(rClaude.getMensaje());
        
        // Probar obtener todos de nuevo, para confirmar que ya aparece el nuevo
        Respuesta rTodos = service.obtenerTodos();
        System.out.println(rTodos.getMensaje());
        System.out.println(rTodos.getResultado());
    }
}