package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.model.OrderStatusType;
import pe.edu.vallegrande.restLosPinos.service.OrderStatusTypeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-status-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderStatusTypeRest {

    private final OrderStatusTypeService orderStatusTypeService;

    // GET /api/order-status-types - Obtener todos los tipos de estado
    @GetMapping
    public ResponseEntity<List<OrderStatusType>> getAll() {
        List<OrderStatusType> orderStatusTypes = orderStatusTypeService.findAll();
        return ResponseEntity.ok(orderStatusTypes);
    }

    // GET /api/order-status-types/{id} - Obtener tipo de estado por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusType> getById(@PathVariable Long id) {
        Optional<OrderStatusType> orderStatusType = orderStatusTypeService.findById(id);
        return orderStatusType.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/order-status-types - Crear nuevo tipo de estado
    @PostMapping
    public ResponseEntity<OrderStatusType> create(@RequestBody OrderStatusType orderStatusType) {
        OrderStatusType createdOrderStatusType = orderStatusTypeService.create(orderStatusType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderStatusType);
    }

    // PUT /api/order-status-types/{id} - Actualizar tipo de estado
    @PutMapping("/{id}")
    public ResponseEntity<OrderStatusType> update(@PathVariable Long id, @RequestBody OrderStatusType orderStatusType) {
        orderStatusType.setIdTypeStatus(id);
        OrderStatusType updatedOrderStatusType = orderStatusTypeService.update(orderStatusType);
        return ResponseEntity.ok(updatedOrderStatusType);
    }

    // DELETE /api/order-status-types/{id} - Eliminar tipo de estado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderStatusTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
