package pe.edu.vallegrande.barberia_macha.rest;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxExporterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.barberia_macha.model.Venta;
import pe.edu.vallegrande.barberia_macha.model.DetalleVenta;
import pe.edu.vallegrande.barberia_macha.model.VentaRequest;
import pe.edu.vallegrande.barberia_macha.service.VentaService;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private static final Logger logger = LoggerFactory.getLogger(VentaController.class);

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> getAllVentas() {
        return ventaService.listarVentas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.getVentaById(id);
        return venta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Venta createVenta(@RequestBody VentaRequest ventaRequest) {
        Long idUsuario = ventaRequest.getIdUsuario();
        List<DetalleVenta> detalles = ventaRequest.getDetalles();
        return ventaService.registrarVenta(idUsuario, detalles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            Venta updatedVenta = ventaService.updateVenta(id, venta);
            return ResponseEntity.ok(updatedVenta);
        } catch (VentaService.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating venta: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.getVentaById(id);
        if (venta.isPresent()) {
            ventaService.deleteVenta(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Venta> logicalDeleteVenta(@PathVariable Long id) {
        try {
            Venta venta = ventaService.logicalDeleteVenta(id);
            return ResponseEntity.ok(venta);
        } catch (VentaService.ResourceConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (VentaService.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Venta> logicalActivateVenta(@PathVariable Long id) {
        try {
            Venta venta = ventaService.logicalActivateVenta(id);
            return ResponseEntity.ok(venta);
        } catch (VentaService.ResourceConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (VentaService.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}