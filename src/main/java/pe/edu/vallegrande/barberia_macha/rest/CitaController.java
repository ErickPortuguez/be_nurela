package pe.edu.vallegrande.barberia_macha.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.barberia_macha.model.Cita;
import pe.edu.vallegrande.barberia_macha.service.CitaService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/citas")
public class CitaController {
    @Autowired
    private CitaService citaService;

    @GetMapping
    public List<Cita> listarCitas() {
        return citaService.listarCitas();
    }

    @GetMapping("/pendientes")
    public List<Map<String, Object>> listarCitasPendientesConDetalles() {
        return citaService.listarCitasPendientesConDetalles();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCita(@PathVariable Long id) {
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        return cita.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Cita> guardarCita(@RequestBody Cita cita) {
        try {
            // Verifica que cliente y barbero están presentes
            if (cita.getCliente() == null || cita.getBarbero() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Cita nuevaCita = citaService.guardarCita(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita citaActualizada) {
        try {
            Cita cita = citaService.actualizarCita(id, citaActualizada);
            return ResponseEntity.ok(cita);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstadoCita(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String nuevoEstado = request.get("nuevoEstado");
            Cita cita = citaService.cambiarEstadoCita(id, nuevoEstado);
            return ResponseEntity.ok(cita);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
