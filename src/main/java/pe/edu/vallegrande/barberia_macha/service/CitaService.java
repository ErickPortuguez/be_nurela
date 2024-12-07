package pe.edu.vallegrande.barberia_macha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.barberia_macha.model.Cita;
import pe.edu.vallegrande.barberia_macha.repository.CitaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public List<Map<String, Object>> listarCitasPendientesConDetalles() {
        return citaRepository.listarCitasPendientesConDetalles();
    }

    public Optional<Cita> obtenerCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    public Cita guardarCita(Cita cita) {
        // Asumiendo que el cliente y el barbero se establecen en la cita
        if (cita.getCliente() == null || cita.getBarbero() == null) {
            throw new RuntimeException("Cliente y Barbero son requeridos");
        }
        return citaRepository.save(cita);
    }


    public Cita actualizarCita(Long id, Cita citaActualizada) {
        return citaRepository.findById(id).map(cita -> {
            cita.setFecha(citaActualizada.getFecha());
            cita.setHora(citaActualizada.getHora());
            cita.setNota(citaActualizada.getNota());
            cita.setEstado(citaActualizada.getEstado());
            cita.setCliente(citaActualizada.getCliente());
            cita.setBarbero(citaActualizada.getBarbero());
            return citaRepository.save(cita);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    // Método para cambiar el estado de la cita
    public Cita cambiarEstadoCita(Long idCita, String nuevoEstado) {
        return citaRepository.findById(idCita).map(cita -> {
            // Actualiza el estado de la cita
            cita.setEstado(nuevoEstado);
            return citaRepository.save(cita);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }


}
