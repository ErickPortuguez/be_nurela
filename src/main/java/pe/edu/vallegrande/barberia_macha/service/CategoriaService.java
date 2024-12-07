package pe.edu.vallegrande.barberia_macha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.barberia_macha.model.Categoria;
import pe.edu.vallegrande.barberia_macha.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria agregarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> obtenerCategoria(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria editarCategoria(Long id, Categoria categoriaActualizada) {
        if (categoriaRepository.existsById(id)) {
            categoriaActualizada.setIdCategoria(id);
            return categoriaRepository.save(categoriaActualizada);
        }
        return null;
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}
