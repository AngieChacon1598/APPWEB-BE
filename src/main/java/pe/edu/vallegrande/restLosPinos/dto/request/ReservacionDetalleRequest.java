package pe.edu.vallegrande.restLosPinos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservacionDetalleRequest {

    private String hourReservation;

    private String dateReservation;

    private Integer amountPeople;

    private String requirements;

    private Integer tableId;

}
