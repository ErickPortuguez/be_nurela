package pe.edu.vallegrande.barberia_macha.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.barberia_macha.model.LoginRequest;
import pe.edu.vallegrande.barberia_macha.model.Usuario;
import pe.edu.vallegrande.barberia_macha.service.UsuarioService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioService.verificarCredenciales(loginRequest.getEmail(), loginRequest.getPassword());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return ResponseEntity.ok(Map.of("message", "Login exitoso", "rol", usuario.getRol(), "idUsuario", usuario.getIdUsuario()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioService.verificarCredenciales(loginRequest.getEmail(), loginRequest.getPassword());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return ResponseEntity.ok(Map.of("message", "Login exitoso", "rol", usuario.getRol(), "idUsuario", usuario.getIdUsuario()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @GetMapping("/barberos")
    public List<Usuario> listarBarberos() {
        return usuarioService.listarBarberos(); // Implementa este método en tu servicio
    }


    @GetMapping("/activos")
    public List<Usuario> listarUsuariosActivos() {
        return usuarioService.listarUsuariosActivos();
    }

    @GetMapping("/inactivos")
    public List<Usuario> listarUsuariosInactivos() {
        return usuarioService.listarUsuariosInactivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getActivo() == null) {
            usuario.setActivo(1); // Asigna como activo si no se especifica
        }
        try {
            Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario usuario = usuarioService.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/recuperar/{id}")
    public ResponseEntity<Usuario> recuperarCuenta(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.recuperarCuenta(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }



}
