package pe.edu.vallegrande.barberia_macha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.vallegrande.barberia_macha.model.Cita;

import java.util.List;
import java.util.Map;

public interface CitaRepository extends JpaRepository<Cita,Long> {
    List<Cita> findByEstado(String estado);
    @Query(value = """
        SELECT new map(
            c.idCita as idCita,
            c.fecha as fecha,
            c.hora as hora,
            c.nota as nota,
            c.estado as estado,
            cliente.nombre as nombreCliente,
            cliente.apellido as apellidoCliente,
            barbero.nombre as nombreBarbero,
            barbero.apellido as apellidoBarbero
        )
        FROM Cita c
        JOIN c.cliente cliente
        JOIN c.barbero barbero
        WHERE c.estado = 'pendiente' AND cliente.rol = 'cliente' AND barbero.rol = 'barbero'
    """)
    List<Map<String, Object>> listarCitasPendientesConDetalles();
}
