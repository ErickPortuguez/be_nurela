package pe.edu.vallegrande.barberia_macha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.vallegrande.barberia_macha.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByActivo(int activo);
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.password = :password")
    Optional<Usuario> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    List<Usuario> findByRol(String rol);

}
