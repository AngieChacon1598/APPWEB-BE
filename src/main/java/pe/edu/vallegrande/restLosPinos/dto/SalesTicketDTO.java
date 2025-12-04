package pe.edu.vallegrande.restLosPinos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para manejar la información de tickets de venta en el sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesTicketDTO {
    private Long ticketId;
    private LocalDateTime saleDate;
    private BigDecimal totalPayment;
    private String delivery;
    private String deliveryAddress;
    private String note;
    private String state;
    private String orderStatus;
    private String paymentType;
    private Long userId;
    private String userName;
    private Long idTypeState;
    private String stateName;
    private Long idPaymentType;
    private String paymentTypeName;
    private List<ProductDetailDTO> productDetails;
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
