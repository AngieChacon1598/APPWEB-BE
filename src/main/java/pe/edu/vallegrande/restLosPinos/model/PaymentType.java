package pe.edu.vallegrande.restLosPinos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_type", schema = "DEVELOPER_ANGI")
public class PaymentType {

    @Id
    @Column(name = "id_payment_type")
    private Long idPaymentType;

    @Column(name = "name", nullable = false, length = 90)
    private String name;
}
