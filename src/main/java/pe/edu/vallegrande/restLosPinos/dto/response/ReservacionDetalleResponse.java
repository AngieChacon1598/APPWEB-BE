package pe.edu.vallegrande.restLosPinos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservacionDetalleResponse {
    private Integer amountPeople;

    private LocalTime hourReservation;

    private LocalDate dateReservation;

    private String requirements;

    private Integer tableId;

    private String imagenMesa;
}
