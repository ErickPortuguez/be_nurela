package pe.edu.vallegrande.barberia_macha.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.barberia_macha.model.Producto;
import pe.edu.vallegrande.barberia_macha.repository.ProductoRepository;
import pe.edu.vallegrande.barberia_macha.service.ProductoService;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
    @GetMapping("/activos")
    public List<Producto> listarProductosActivos() {
        return productoService.listarProductosActivos();
    }

    @GetMapping("/inactivos")
    public List<Producto> listarProductosInactivos() {
        return productoService.listarProductosInactivos();
    }
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setImagen(productoDetails.getImagen());
            producto.setNombre(productoDetails.getNombre());
            producto.setDescripcion(productoDetails.getDescripcion());
            producto.setPrecio(productoDetails.getPrecio());
            producto.setStock(productoDetails.getStock());
            producto.setUnidadMedida(productoDetails.getUnidadMedida());
            producto.setFechaIngreso(productoDetails.getFechaIngreso());
            producto.setFechaExpiracion(productoDetails.getFechaExpiracion());
            producto.setEstado(productoDetails.getEstado());
            producto.setCategoria(productoDetails.getCategoria());
            Producto updatedProducto = productoRepository.save(producto);
            return ResponseEntity.ok(updatedProducto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/restaurar/{id}")
    public ResponseEntity<Void> restaurarProducto(@PathVariable Long id) {
        // Llamada al servicio para restaurar el producto
        productoService.restaurarProducto(id);
        // Retorna un estado 204 No Content si la operación fue exitosa
        return ResponseEntity.noContent().build();
    }

}