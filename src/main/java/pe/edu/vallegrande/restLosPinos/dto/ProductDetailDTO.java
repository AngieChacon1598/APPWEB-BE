package pe.edu.vallegrande.restLosPinos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para manejar los detalles de productos en el sistema.
 * Incluye información tanto para tickets como para menús.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    private Long idDetailProduct;
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private BigDecimal amount;
    private Long ticketId;
    private MenuDTO menu;
}
