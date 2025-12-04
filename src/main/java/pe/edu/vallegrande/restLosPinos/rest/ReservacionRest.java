package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.dto.request.ReservacionRequest;
import pe.edu.vallegrande.restLosPinos.dto.response.HistoryReservationResponse;
import pe.edu.vallegrande.restLosPinos.dto.response.ReservacionDetalleResponse;
import pe.edu.vallegrande.restLosPinos.dto.response.ReservacionResponse;
import pe.edu.vallegrande.restLosPinos.service.ReservacionService;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/reservaciones")
@RestController
public class ReservacionRest {
    private final ReservacionService reservacionService;

    @PostMapping("/agregar-reservacion")
    public ResponseEntity<String> guardarReservacion(
            @RequestBody ReservacionRequest reservacionRequest
    ) {
        return reservacionService.realizarReservacion(reservacionRequest);
    }

    @GetMapping("/ver-mesas-disponibles")
    public ResponseEntity<List<HistoryReservationResponse>> verMesasDisponibles(
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") String fecha,
            @RequestParam(value = "mesa") Integer mesa
    ) {
        return reservacionService.verMesasDisponibles(fecha, mesa);
    }

    @GetMapping("/listar-reservaciones-usuario")
    public ResponseEntity<List<ReservacionResponse>> listaReservacionesUsuario(
            @RequestParam(value = "idUsuario") Integer idUsuario
    ) {
        return reservacionService.listaReservacionesUsuario(idUsuario);
    }

    @GetMapping("/listar-reservaciones-detalle")
    public ResponseEntity<List<ReservacionDetalleResponse>> listaReservacionesDetalle(
            @RequestParam(value = "idReserva") Integer idReserva
    ) {
        return reservacionService.listaReservacionesDetalle(idReserva);
    }

    @GetMapping("/listar-reservaciones")
    public  ResponseEntity<List<ReservacionResponse>> listaReservaciones(
            @RequestParam(value = "estado", required = false) String estado
    ) {
        return reservacionService.listaReservaciones(estado);
    }

    @PostMapping("/aprobar-reservaciones")
    public  ResponseEntity<String> aprobarReservaciones(
            @RequestParam(value = "idReserva", required = false) Integer idReserva
    ) {
        return reservacionService.aprobarReservaciones(idReserva);
    }

    @PostMapping("/cancelar-reservaciones")
    public  ResponseEntity<String> cancelarReservaciones(
            @RequestParam(value = "idReserva", required = false) Integer idReserva
    ) {
        return reservacionService.cancelarReservaciones(idReserva);
    }

}
