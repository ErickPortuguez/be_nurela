package pe.edu.vallegrande.barberia_macha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.barberia_macha.model.Venta;

import java.util.Collection;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Collection<Object> findByEstado(String estado);
}
