package pe.edu.vallegrande.restLosPinos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MesaRestaurantRequest {
    private Integer numberTable;
    private Integer ability;
    private String descripcion;
    private String imagen;
}
