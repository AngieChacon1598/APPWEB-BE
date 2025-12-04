package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.model.PaymentType;
import pe.edu.vallegrande.restLosPinos.service.PaymentTypeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentTypeRest {

    private final PaymentTypeService paymentTypeService;

    // GET /api/payment-types - Obtener todos los tipos de pago
    @GetMapping
    public ResponseEntity<List<PaymentType>> getAll() {
        List<PaymentType> paymentTypes = paymentTypeService.findAll();
        return ResponseEntity.ok(paymentTypes);
    }

    // GET /api/payment-types/{id} - Obtener tipo de pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentType> getById(@PathVariable Long id) {
        Optional<PaymentType> paymentType = paymentTypeService.findById(id);
        return paymentType.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/payment-types - Crear nuevo tipo de pago
    @PostMapping
    public ResponseEntity<PaymentType> create(@RequestBody PaymentType paymentType) {
        PaymentType createdPaymentType = paymentTypeService.create(paymentType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPaymentType);
    }

    // PUT /api/payment-types/{id} - Actualizar tipo de pago
    @PutMapping("/{id}")
    public ResponseEntity<PaymentType> update(@PathVariable Long id, @RequestBody PaymentType paymentType) {
        paymentType.setIdPaymentType(id);
        PaymentType updatedPaymentType = paymentTypeService.update(paymentType);
        return ResponseEntity.ok(updatedPaymentType);
    }

    // DELETE /api/payment-types/{id} - Eliminar tipo de pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
