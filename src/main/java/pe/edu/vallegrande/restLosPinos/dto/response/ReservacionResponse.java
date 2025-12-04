package pe.edu.vallegrande.restLosPinos.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservacionResponse {
    private Integer idReservacion;
    private String namereservation;
    private String estado;
    private LocalDateTime fechaCreacion;
}
