package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.model.PaymentType;
import pe.edu.vallegrande.restLosPinos.repository.PaymentTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;

    // Listar todos los tipos de pago
    public List<PaymentType> findAll() {
        return paymentTypeRepository.findAll();
    }

    // Buscar tipo de pago por ID
    public Optional<PaymentType> findById(Long id) {
        return paymentTypeRepository.findById(id);
    }

    // Crear nuevo tipo de pago
    public PaymentType create(PaymentType paymentType) {
        return paymentTypeRepository.save(paymentType);
    }

    // Actualizar tipo de pago
    public PaymentType update(PaymentType paymentType) {
        return paymentTypeRepository.save(paymentType);
    }

    // Eliminar tipo de pago
    public void delete(Long id) {
        paymentTypeRepository.deleteById(id);
    }
}
