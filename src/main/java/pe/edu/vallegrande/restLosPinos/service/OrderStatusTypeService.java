package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.model.OrderStatusType;
import pe.edu.vallegrande.restLosPinos.repository.OrderStatusTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderStatusTypeService {

    private final OrderStatusTypeRepository orderStatusTypeRepository;

    // Listar todos los tipos de estado
    public List<OrderStatusType> findAll() {
        return orderStatusTypeRepository.findAll();
    }

    // Buscar tipo de estado por ID
    public Optional<OrderStatusType> findById(Long id) {
        return orderStatusTypeRepository.findById(id);
    }

    // Crear nuevo tipo de estado
    public OrderStatusType create(OrderStatusType orderStatusType) {
        return orderStatusTypeRepository.save(orderStatusType);
    }

    // Actualizar tipo de estado
    public OrderStatusType update(OrderStatusType orderStatusType) {
        return orderStatusTypeRepository.save(orderStatusType);
    }

    // Eliminar tipo de estado
    public void delete(Long id) {
        orderStatusTypeRepository.deleteById(id);
    }
}
