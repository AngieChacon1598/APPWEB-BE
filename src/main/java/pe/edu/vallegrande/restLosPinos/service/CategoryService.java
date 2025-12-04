package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.restLosPinos.model.Category;
import pe.edu.vallegrande.restLosPinos.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Listar todas las categorías activas
    public List<Category> findAllActive() {
        return categoryRepository.findActiveCategories();
    }

    // Listar todas las categorías inactivas
    public List<Category> findAllInactive() {
        return categoryRepository.findInactiveCategories();
    }

    // Listar todas las categorías
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Buscar categoría por ID
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    // Crear nueva categoría
    @Transactional
    public Category create(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        category.setState("1"); // activo por defecto
        return categoryRepository.save(category);
    }

    // Actualizar categoría
    @Transactional
    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    // Eliminación lógica
    @Transactional
    public void logicalDelete(Long id) {
        categoryRepository.findById(id).ifPresent(category -> {
            category.setState("0"); // inactivo
            categoryRepository.save(category);
        });
    }

    // Restaurar categoría
    @Transactional
    public void restore(Long id) {
        categoryRepository.findById(id).ifPresent(category -> {
            category.setState("1"); // activo
            categoryRepository.save(category);
        });
    }

    // Eliminación física
    @Transactional
    public void physicalDelete(Long id) {
        categoryRepository.deleteById(id);
    }

    // Buscar por nombre
    public List<Category> findByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}
