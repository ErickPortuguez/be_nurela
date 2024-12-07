package pe.edu.vallegrande.barberia_macha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.barberia_macha.model.Cita;
import pe.edu.vallegrande.barberia_macha.model.Pago;
import pe.edu.vallegrande.barberia_macha.repository.CitaRepository;
import pe.edu.vallegrande.barberia_macha.repository.PagoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CitaRepository citaRepository;

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    public List<Map<String, Object>> obtenerPagosPorParametros(Long idBarbero, LocalDate fechaInicio, LocalDate fechaFin, Double porcentaje) {
        List<Object[]> resultados = pagoRepository.obtenerPagosPorParametros(idBarbero, fechaInicio, fechaFin, porcentaje);
        return resultados.stream().map(resultado -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idPago", resultado[0]);
            map.put("fechaPago", resultado[1]);
            map.put("horaPago", resultado[2]);
            map.put("idBarbero", resultado[3]);
            map.put("nombreBarbero", resultado[4]);
            map.put("apellidoBarbero", resultado[5]);
            map.put("fechaCita", resultado[6]);
            map.put("montoTotalCita", resultado[7]);
            map.put("montoBarbero", resultado[8]);
            return map;
        }).toList();
    }


    public Optional<Pago> obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public Pago guardarPago(Pago pago) {
        Cita cita = pago.getCita();

        if (!"pendiente".equals(cita.getEstado())) {
            throw new RuntimeException("La cita ya ha sido completada o cancelada.");
        }

        // Guarda el pago
        Pago nuevoPago = pagoRepository.save(pago);

        // Actualiza el estado de la cita a "terminado"
        cita.setEstado("terminado");
        citaRepository.save(cita);

        return nuevoPago;
    }

    public Pago actualizarPago(Long id, Pago pagoActualizado) {
        return pagoRepository.findById(id).map(pago -> {
            pago.setCita(pagoActualizado.getCita());
            pago.setCorteRealizado(pagoActualizado.getCorteRealizado());
            pago.setMonto(pagoActualizado.getMonto());
            pago.setFechaPago(pagoActualizado.getFechaPago());
            pago.setHoraPago(pagoActualizado.getHoraPago());
            pago.setStatus(pagoActualizado.getStatus());
            return pagoRepository.save(pago);
        }).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    // Método para eliminar lógicamente un pago
    public Pago eliminarLogicamentePago(Long idPago) {
        return pagoRepository.findById(idPago).map(pago -> {
            pago.setStatus(0);  // Cambiar el estado a 0 para eliminación lógica
            return pagoRepository.save(pago);
        }).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    // Método para reactivar un pago
    public Pago reactivarPago(Long idPago) {
        return pagoRepository.findById(idPago).map(pago -> {
            // Cambia el status a 1
            pago.setStatus(1);
            return pagoRepository.save(pago); // Guarda los cambios
        }).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }



}
