package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.SalesTicket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesTicketRepository extends JpaRepository<SalesTicket, Long> {

    // Buscar ventas activas
    @Query("SELECT s FROM SalesTicket s WHERE s.state = 'A'")
    List<SalesTicket> findActiveSales();
    
    @Query("SELECT s FROM SalesTicket s WHERE s.state = 'A'")
    Page<SalesTicket> findActiveSales(Pageable pageable);

    // Buscar ventas inactivas
    @Query("SELECT s FROM SalesTicket s WHERE s.state = 'I'")
    List<SalesTicket> findInactiveSales();
    
    @Query("SELECT s FROM SalesTicket s WHERE s.state = 'I'")
    Page<SalesTicket> findInactiveSales(Pageable pageable);

    // Buscar ventas por usuario
    @Query("SELECT s FROM SalesTicket s WHERE s.userId = :userId AND s.state = 'A'")
    List<SalesTicket> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT s FROM SalesTicket s WHERE s.userId = :userId AND s.state = 'A'")
    Page<SalesTicket> findByUserId(@Param("userId") Long userId, Pageable pageable);

    // Buscar ventas por estado
    @Query("SELECT s FROM SalesTicket s WHERE s.orderStatusType.id = :statusId AND s.state = 'A'")
    List<SalesTicket> findByStatusId(@Param("statusId") Long statusId);
    
    @Query("SELECT s FROM SalesTicket s WHERE s.orderStatusType.id = :statusId AND s.state = 'A'")
    Page<SalesTicket> findByStatusId(@Param("statusId") Long statusId, Pageable pageable);

    // Buscar ventas por rango de fechas
    @Query("SELECT s FROM SalesTicket s WHERE s.saleDate BETWEEN :startDate AND :endDate AND s.state = 'A'")
    List<SalesTicket> findByDateRange(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT s FROM SalesTicket s WHERE s.saleDate BETWEEN :startDate AND :endDate AND s.state = 'A'")
    Page<SalesTicket> findByDateRange(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    // Buscar ventas con delivery
    @Query("SELECT s FROM SalesTicket s WHERE s.delivery = 'SI' AND s.state = 'A'")
    List<SalesTicket> findDeliverySales();
    
    @Query("SELECT s FROM SalesTicket s WHERE s.delivery = 'SI' AND s.state = 'A'")
    Page<SalesTicket> findDeliverySales(Pageable pageable);
    
    // Método para búsqueda por ID incluyendo inactivos
    @Query("SELECT s FROM SalesTicket s WHERE s.ticketId = :id")
    Optional<SalesTicket> findByIdIncludingInactive(@Param("id") Long id);
    
    // Eliminación lógica
    @Modifying
    @Query("UPDATE SalesTicket s SET s.state = 'I' WHERE s.ticketId = :id")
    void softDelete(@Param("id") Long id);
    
    // Restaurar
    @Modifying
    @Query("UPDATE SalesTicket s SET s.state = 'A' WHERE s.ticketId = :id")
    void restore(@Param("id") Long id);
    
    // Buscar por ID de pago
    @Query("SELECT s FROM SalesTicket s WHERE s.paymentType.id = :paymentTypeId AND s.state = 'A'")
    List<SalesTicket> findByPaymentTypeId(@Param("paymentTypeId") Long paymentTypeId);
    
    // Contar ventas activas
    @Query("SELECT COUNT(s) FROM SalesTicket s WHERE s.state = 'A'")
    long countActiveSales();
    
    // Obtener ventas recientes
    @Query("SELECT s FROM SalesTicket s WHERE s.state = 'A' ORDER BY s.saleDate DESC")
    Page<SalesTicket> findRecentSales(Pageable pageable);

    // Obtener todas las ventas con sus detalles para reporte (todas o por ID)
    @Query("SELECT DISTINCT s FROM SalesTicket s " +
           "LEFT JOIN FETCH s.productDetails pd " +
           "LEFT JOIN FETCH pd.menu " +
           "WHERE (:ticketId IS NULL OR s.ticketId = :ticketId) " +
           "ORDER BY s.ticketId, pd.idDetailProduct")
    List<SalesTicket> findSalesForReport(@Param("ticketId") Long ticketId);
}
