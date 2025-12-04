package pe.edu.vallegrande.restLosPinos.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sales_ticket", schema = "DEVELOPER_ANGI")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "ticketId"
)
public class SalesTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "total_payment", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPayment;

    @Column(name = "delivery", length = 2)
    private String delivery;

    @Column(name = "delivery_address", length = 150)
    private String deliveryAddress;

    @Column(name = "note", length = 200)
    private String note;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "id_type_state", nullable = false, insertable = false, updatable = false)
    private Long idTypeState;

    @Column(name = "id_payment_type", nullable = false, insertable = false, updatable = false)
    private Long idPaymentType;

    @Column(name = "state", columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String state;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_state", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OrderStatusType orderStatusType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payment_type", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PaymentType paymentType;

    @OneToMany(mappedBy = "salesTicket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("salesTicket-productDetails")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ProductDetail> productDetails;

    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = "A";
        }
        if (this.saleDate == null) {
            this.saleDate = LocalDateTime.now();
        }
    }
}
