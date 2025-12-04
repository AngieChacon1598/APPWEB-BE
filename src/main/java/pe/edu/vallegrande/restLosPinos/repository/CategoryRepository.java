package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Buscar categorías activas
    @Query("SELECT c FROM Category c WHERE c.state = '1'")
    List<Category> findActiveCategories();

    // Buscar categorías inactivas
    @Query("SELECT c FROM Category c WHERE c.state = '0'")
    List<Category> findInactiveCategories();

    // Buscar por nombre
    List<Category> findByNameContainingIgnoreCase(String name);
}
