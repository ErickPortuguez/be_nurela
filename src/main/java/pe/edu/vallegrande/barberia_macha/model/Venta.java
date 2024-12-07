package pe.edu.vallegrande.barberia_macha.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas", schema = "gracielacaceres")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private LocalDate fechaVenta;
    private Double montoTotal;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @Transient
    private String adminNombreCompleto;

    @Transient
    private String clienteNombreCompleto;

    private String active;

    private char estado = 'A'; // Default value

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public String getAdminNombreCompleto() {
        return adminNombreCompleto;
    }

    public void setAdminNombreCompleto(String adminNombreCompleto) {
        this.adminNombreCompleto = adminNombreCompleto;
    }

    public String getClienteNombreCompleto() {
        return clienteNombreCompleto;
    }

    public void setClienteNombreCompleto(String clienteNombreCompleto) {
        this.clienteNombreCompleto = clienteNombreCompleto;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }
}