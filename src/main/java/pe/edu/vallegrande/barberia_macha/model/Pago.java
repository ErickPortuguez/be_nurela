package pe.edu.vallegrande.barberia_macha.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pagos", schema = "OMARRIVERAFELIX")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Column(name = "corte_realizado", nullable = false)
    private String corteRealizado;

    @Column(name = "monto", nullable = false)
    private Double monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "hora_pago", nullable = false)
    private String horaPago;

    @Column(name = "status", nullable = false)
    private Integer status = 1;  // Por defecto, el pago está activo (1)

    // Getters y Setters
    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public String getCorteRealizado() {
        return corteRealizado;
    }

    public void setCorteRealizado(String corteRealizado) {
        this.corteRealizado = corteRealizado;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getHoraPago() {
        return horaPago;
    }

    public void setHoraPago(String horaPago) {
        this.horaPago = horaPago;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
