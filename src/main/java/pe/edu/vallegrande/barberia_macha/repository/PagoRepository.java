package pe.edu.vallegrande.barberia_macha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.barberia_macha.model.Pago;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query(value = """
        SELECT p.id_pago AS idPago, p.fecha_pago AS fechaPago, p.hora_pago AS horaPago, 
               u.id_usuario AS idBarbero, u.nombre AS nombreBarbero, u.apellido AS apellidoBarbero, 
               c.fecha AS fechaCita, p.monto AS montoTotalCita, (p.monto * :porcentaje) AS montoBarbero
        FROM pagos p
        JOIN citas c ON p.id_cita = c.id_cita
        JOIN usuario u ON c.id_barbero = u.id_usuario
        WHERE u.id_usuario = :idBarbero
        AND p.fecha_pago BETWEEN :fechaInicio AND :fechaFin
        ORDER BY p.fecha_pago
        """, nativeQuery = true)
    List<Object[]> obtenerPagosPorParametros(
            @Param("idBarbero") Long idBarbero,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("porcentaje") Double porcentaje
    );

}
