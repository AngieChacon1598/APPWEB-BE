package pe.edu.vallegrande.restLosPinos.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservacionRequest {

    private Integer idUsuario;

    private String nameReservation;

    private String dateReservation;

    private List<ReservacionDetalleRequest> detallesRequest;


}
