package pe.edu.vallegrande.restLosPinos.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_detail", schema = "DEVELOPER_ANGI")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "idDetailProduct"
)
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail_product")
    private Long idDetailProduct;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "ticket_id", nullable = false, insertable = false, updatable = false)
    private Long ticketId;

    @Column(name = "menu_id", nullable = false, insertable = false, updatable = false)
    private Long menuId;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference("salesTicket-productDetails")
    private SalesTicket salesTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Menu menu;
}
