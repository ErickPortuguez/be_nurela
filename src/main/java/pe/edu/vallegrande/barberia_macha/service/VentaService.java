package pe.edu.vallegrande.barberia_macha.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.edu.vallegrande.barberia_macha.model.DetalleVenta;
import pe.edu.vallegrande.barberia_macha.model.Producto;
import pe.edu.vallegrande.barberia_macha.model.Usuario;
import pe.edu.vallegrande.barberia_macha.model.Venta;
import pe.edu.vallegrande.barberia_macha.repository.DetalleVentaRepository;
import pe.edu.vallegrande.barberia_macha.repository.ProductoRepository;
import pe.edu.vallegrande.barberia_macha.repository.UsuarioRepository;
import pe.edu.vallegrande.barberia_macha.repository.VentaRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VentaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public Venta registrarVenta(Long idUsuario, List<DetalleVenta> detalles) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + idUsuario));

        Venta venta = new Venta();
        venta.setFechaVenta(LocalDate.now());
        venta.setMontoTotal(0.0);
        venta.setUsuario(usuario);

        double montoTotal = 0.0;
        for (DetalleVenta detalle : detalles) {
            Producto producto = productoRepository.findById(detalle.getProducto().getIdProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto not found with id " + detalle.getProducto().getIdProducto()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            detalle.setVenta(venta);
            montoTotal += subtotal;
        }

        venta.setMontoTotal(montoTotal);
        venta = ventaRepository.save(venta);

        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(venta);
            detalleVentaRepository.save(detalle);
        }

        return venta;
    }

    public List<Venta> listarVentas() {
        List<Venta> ventas = ventaRepository.findAll();
        setTransientFields(ventas);
        return ventas;
    }

    public Optional<Venta> getVentaById(Long id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        venta.ifPresent(this::setTransientFields);
        return venta;
    }

    public Venta updateVenta(Long id, Venta ventaUpdated) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta not found with id " + id));

        venta.setUsuario(ventaUpdated.getUsuario());
        venta.setFechaVenta(ventaUpdated.getFechaVenta() != null ? ventaUpdated.getFechaVenta() : LocalDate.now());
        updateVentaDetails(venta, ventaUpdated.getDetalles());
        calculateVentaTotals(venta);

        Venta savedVenta = ventaRepository.save(venta);
        setTransientFields(savedVenta);
        return savedVenta;
    }

    private void updateVentaDetails(Venta venta, List<DetalleVenta> updatedDetails) {
        Map<Long, DetalleVenta> currentDetailsMap = venta.getDetalles().stream()
                .collect(Collectors.toMap(DetalleVenta::getIdDetalleVenta, detail -> detail));

        for (DetalleVenta detail : updatedDetails) {
            if (detail.getIdDetalleVenta() == null) {
                detail.setVenta(venta);
                venta.getDetalles().add(detail);
            } else if (currentDetailsMap.containsKey(detail.getIdDetalleVenta())) {
                DetalleVenta existingDetail = currentDetailsMap.get(detail.getIdDetalleVenta());
                existingDetail.setProducto(detail.getProducto());
                existingDetail.setCantidad(detail.getCantidad());
                existingDetail.setPrecioUnitario(detail.getPrecioUnitario());
                existingDetail.setSubtotal(detail.getSubtotal());
            }
        }

        venta.getDetalles().removeIf(detail -> !updatedDetails.stream()
                .map(DetalleVenta::getIdDetalleVenta)
                .collect(Collectors.toList())
                .contains(detail.getIdDetalleVenta()));
    }

    private void calculateVentaTotals(Venta venta) {
        double total = 0;
        for (DetalleVenta detail : venta.getDetalles()) {
            Producto producto = productoRepository.findById(detail.getProducto().getIdProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto not found with id " + detail.getProducto().getIdProducto()));
            double subtotal = detail.getPrecioUnitario() * detail.getCantidad();
            detail.setSubtotal(subtotal);
            total += subtotal;
        }
        venta.setMontoTotal(total);
    }

    private void setTransientFields(List<Venta> ventas) {
        for (Venta venta : ventas) {
            setTransientFields(venta);
        }
    }

    private void setTransientFields(Venta venta) {
        if (venta.getUsuario() != null) {
            venta.setAdminNombreCompleto(venta.getUsuario().getNombre() + " " + venta.getUsuario().getApellido());
            venta.setClienteNombreCompleto(venta.getUsuario().getNombre() + " " + venta.getUsuario().getApellido());
        }
    }

    public void deleteVenta(Long id) {
        ventaRepository.deleteById(id);
    }

    public Venta logicalDeleteVenta(Long id) {
        return ventaRepository.findById(id)
                .map(venta -> {
                    if ('I' == venta.getEstado()) {
                        throw new ResourceConflictException("Venta with id " + id + " is already inactive");
                    }
                    venta.setEstado('I');
                    return ventaRepository.save(venta);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Venta not found with id " + id));
    }

    public Venta logicalActivateVenta(Long id) {
        return ventaRepository.findById(id)
                .map(venta -> {
                    if ('A' == venta.getEstado()) {
                        throw new ResourceConflictException("Venta with id " + id + " is already active");
                    }
                    venta.setEstado('A');
                    return ventaRepository.save(venta);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Venta not found with id " + id));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class ResourceConflictException extends RuntimeException {
        public ResourceConflictException(String message) {
            super(message);
        }
    }
}