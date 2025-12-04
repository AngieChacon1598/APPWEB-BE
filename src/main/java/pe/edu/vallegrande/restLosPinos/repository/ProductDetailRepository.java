package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.ProductDetail;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    // Buscar detalles por ticket
    @Query("SELECT pd FROM ProductDetail pd WHERE pd.ticketId = :ticketId")
    List<ProductDetail> findByTicketId(@Param("ticketId") Long ticketId);

    // Buscar detalles por producto
    @Query("SELECT pd FROM ProductDetail pd WHERE pd.menuId = :menuId")
    List<ProductDetail> findByMenuId(@Param("menuId") Long menuId);

    // Buscar productos más vendidos
    @Query("SELECT pd.menuId, SUM(pd.amount) as total FROM ProductDetail pd GROUP BY pd.menuId ORDER BY total DESC")
    List<Object[]> findMostSoldProducts();
}
