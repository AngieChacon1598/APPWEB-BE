package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.OrderStatusType;

@Repository
public interface OrderStatusTypeRepository extends JpaRepository<OrderStatusType, Long> {
}
