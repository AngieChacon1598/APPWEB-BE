package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.Menu;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE m.state = 1")
    List<Menu> findActiveProducts();

    @Query("SELECT m FROM Menu m WHERE m.state = 0")
    List<Menu> findInactiveProducts();

    @Query("SELECT m FROM Menu m WHERE m.categoryId = :categoryId AND m.state = 1")
    List<Menu> findByCategoryId(@Param("categoryId") Long categoryId);

    List<Menu> findByNameContainingIgnoreCase(String name);

    @Query("SELECT m FROM Menu m WHERE m.price BETWEEN :minPrice AND :maxPrice AND m.state = 1")
    List<Menu> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    // Método para filtrar productos por estado: 1=activos, 0=inactivos, -1=todos
    @Query("SELECT m FROM Menu m WHERE (:stateFilter = -1 OR m.state = :stateFilter) ORDER BY m.menuId")
    List<Menu> findByStateFilter(@Param("stateFilter") Integer stateFilter);
}
