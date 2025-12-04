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
@Table(name = "order_status_type", schema = "DEVELOPER_ANGI")
public class OrderStatusType {

    @Id
    @Column(name = "id_type_status")
    private Long idTypeStatus;

    @Column(name = "name", nullable = false, length = 90)
    private String name;
}
