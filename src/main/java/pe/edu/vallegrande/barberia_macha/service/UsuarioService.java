package pe.edu.vallegrande.barberia_macha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.barberia_macha.model.Usuario;
import pe.edu.vallegrande.barberia_macha.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findByActivo(1); // 1 indica que están activos
    }

    public List<Usuario> listarUsuariosInactivos() {
        return usuarioRepository.findByActivo(0); // 0 indica que están inactivos
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setTipoDeDocumento(usuarioActualizado.getTipoDeDocumento());
            usuario.setNumeroDeDocumento(usuarioActualizado.getNumeroDeDocumento());
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setApellido(usuarioActualizado.getApellido());
            usuario.setCelular(usuarioActualizado.getCelular());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setPassword(usuarioActualizado.getPassword());
            usuario.setRol(usuarioActualizado.getRol());
            usuario.setActivo(usuarioActualizado.getActivo());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setActivo(0);  // Eliminación lógica
            usuarioRepository.save(usuario);
        });
    }

    public Usuario recuperarCuenta(Long id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            // Verificar si el usuario está inactivo
            if (usuario.getActivo() == 0) {
                usuario.setActivo(1); // Restablecer a activo
                return usuarioRepository.save(usuario);
            } else {
                throw new RuntimeException("El usuario ya está activo");
            }
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    public Optional<Usuario> verificarCredenciales(String email, String password) {
        return usuarioRepository.findByEmailAndPassword(email, password);
    }

    public List<Usuario> listarBarberos() {
        return usuarioRepository.findByRol("barbero"); // Asegúrate de tener este método en tu repositorio
    }


}
