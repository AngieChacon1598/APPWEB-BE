package pe.edu.vallegrande.restLosPinos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para manejar la información de menús en el sistema.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO {
    private Long menuId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imagenUrl;
}
