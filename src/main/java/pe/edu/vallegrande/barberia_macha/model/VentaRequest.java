package pe.edu.vallegrande.barberia_macha.model;

import java.util.List;

public class VentaRequest {
    private Long idUsuario;
    private List<DetalleVenta> detalles;

    // Getters y Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}
