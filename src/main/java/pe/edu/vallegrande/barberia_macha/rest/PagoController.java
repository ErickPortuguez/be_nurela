package pe.edu.vallegrande.barberia_macha.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.barberia_macha.model.Cita;
import pe.edu.vallegrande.barberia_macha.model.Pago;
import pe.edu.vallegrande.barberia_macha.service.CitaService;
import pe.edu.vallegrande.barberia_macha.service.PagoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @Autowired
    private CitaService citaService;  // Injecting CitaService

    @GetMapping
    public List<Pago> listarPagos() {
        return pagoService.listarPagos();
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Map<String, Object>>> obtenerPagosPorParametros(
            @RequestParam Long idBarbero,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam Double porcentaje) {

        List<Map<String, Object>> pagos = pagoService.obtenerPagosPorParametros(idBarbero, fechaInicio, fechaFin, porcentaje);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<Map<String, Object>> obtenerDetallePago(@PathVariable Long id) {
        Optional<Pago> pagoOpt = pagoService.obtenerPagoPorId(id);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("pago", pago);
            response.put("cita", pago.getCita());
            response.put("barbero", pago.getCita().getBarbero());  // Asumiendo que hay un método getBarbero() en la entidad Cita.
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPago(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.obtenerPagoPorId(id);
        return pago.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> guardarPago(@RequestBody Pago pago) {
        try {
            // Validar si la cita existe
            Optional<Cita> citaOpt = citaService.obtenerCitaPorId(pago.getCita().getIdCita());
            if (!citaOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Asignar la cita al pago y guardar
            pago.setCita(citaOpt.get());
            Pago nuevoPago = pagoService.guardarPago(pago);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long id, @RequestBody Pago pagoActualizado) {
        try {
            Pago pago = pagoService.actualizarPago(id, pagoActualizado);
            return ResponseEntity.ok(pago);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Método para eliminar lógicamente un pago
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<Pago> eliminarPagoLogicamente(@PathVariable Long id) {
        try {
            Pago pago = pagoService.eliminarLogicamentePago(id);
            return ResponseEntity.ok(pago);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<Pago> reactivarPago(@PathVariable Long id) {
        try {
            Pago pagoReactivado = pagoService.reactivarPago(id);
            return ResponseEntity.ok(pagoReactivado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
