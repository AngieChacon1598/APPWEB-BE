package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.dto.request.ReservacionDetalleRequest;
import pe.edu.vallegrande.restLosPinos.dto.request.ReservacionRequest;
import pe.edu.vallegrande.restLosPinos.dto.response.HistoryReservationResponse;
import pe.edu.vallegrande.restLosPinos.dto.response.ReservacionDetalleResponse;
import pe.edu.vallegrande.restLosPinos.dto.response.ReservacionResponse;
import pe.edu.vallegrande.restLosPinos.exceptions.GlobalException;
import pe.edu.vallegrande.restLosPinos.exceptions.UsuarioException;
import pe.edu.vallegrande.restLosPinos.model.*;
import pe.edu.vallegrande.restLosPinos.repository.*;
import pe.edu.vallegrande.restLosPinos.utils.ConstansApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservacionService {

    private final ReservacionRepository reservacionRepository;
    private final RestaurantUserRepository restaurantUserRepository;
    private final TypeReservationRepository typeReservationRepository;
    private final MesaRestauranteRepository mesaRestauranteRepository;
    private final ReservacionDetalleRepository reservacionDetalleRepository;
    private final HistoryReservationRepository historyReservationRepository;


    public ResponseEntity<String> realizarReservacion(ReservacionRequest reservacionRequest) {
        try {

            Reservacion reservacion = guardarReservacion(reservacionRequest);
            for (ReservacionDetalleRequest request : reservacionRequest.getDetallesRequest()) {
                guardarDetalleReserva(request, reservacion);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Reserva creada con exito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private Reservacion guardarReservacion(ReservacionRequest reservacion) {
        RestaurantUser usuario = restaurantUserRepository
                .findById(reservacion.getIdUsuario()).orElseThrow(() -> new UsuarioException(reservacion.getIdUsuario()));

        TypeReservation tipo = typeReservationRepository
                .findById(ConstansApi.RESERVADO).orElseThrow(() -> new GlobalException("No se encontro el tipo de reservacion", 404));

        Reservacion reser = Reservacion.builder()
                .restaurantUser(usuario)
                .idTypeReservation(tipo)
                .nameReservation(reservacion.getNameReservation())
                .dateCreation(LocalDateTime.now())
                .state(ConstansApi.ACTIVO)
                .build();

        return reservacionRepository.save(reser);
    }

    private ReservacionDetalle guardarDetalleReserva(ReservacionDetalleRequest request, Reservacion reservacion) {
        MesaRestaurante mesa = mesaRestauranteRepository
                .findById(request.getTableId()).orElseThrow(() -> new GlobalException("No se encontro la mesa " + request.getTableId(), 404));

        if (mesa.getState().equals(ConstansApi.INACTIVO)) {
            throw new GlobalException("La mesa esta inactiva", 404);
        }

        LocalTime hora = converHoraDto(request.getHourReservation());
        LocalDate fecha = converFechaDto(request.getDateReservation());

        boolean disponible = comprobarDisponibilidadMesas(mesa.getIdTable(), fecha, hora);
        if (!disponible) {
            throw new GlobalException("La mesa ya está reservada a esa hora " + request.getHourReservation(), 400);
        }

        ReservacionDetalle detalle = ReservacionDetalle.builder()
                .amountPeople(request.getAmountPeople())
                .requirements(request.getRequirements())
                .hourReservation(converHoraDto(request.getHourReservation()))
                .dateReservation(converFechaDto(request.getDateReservation()))
                .tableId(mesa)
                .reservations(reservacion)
                .build();

        return reservacionDetalleRepository.save(detalle);
    }

    private boolean comprobarDisponibilidadMesas(Integer idMesa, LocalDate fecha, LocalTime hora) {
        LocalTime inicio = hora.minusMinutes(59);
        LocalTime fin = hora.plusMinutes(59);

        List<ReservacionDetalle> reservas = reservacionDetalleRepository
                .findByTableId_IdTableAndDateReservationAndHourReservationBetween(idMesa, fecha, inicio, fin);

        return reservas.isEmpty();
    }

    private LocalDate converFechaDto(String date) {
        DateTimeFormatter daterFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, daterFormatter);
    }

    private LocalTime converHoraDto(String hour) {
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(hour, hourFormatter);
    }

    public ResponseEntity<List<HistoryReservationResponse>> verMesasDisponibles(String fecha, Integer mesa) {
        List<HistoryReservationResponse> history = historyReservationRepository.listarReservaciones(fecha, mesa);
    return ResponseEntity.ok(history);
    }

    public ResponseEntity<List<ReservacionResponse>> listaReservacionesUsuario(Integer idUsuario) {
        List<ReservacionResponse> responses = reservacionRepository.findByRestaurantUser_IdUserOrderByIdReservationDesc(idUsuario)
                .stream()
                .map(reservacion -> ReservacionResponse.builder()
                        .idReservacion(reservacion.getIdReservation())
                        .namereservation(reservacion.getNameReservation())
                        .fechaCreacion(reservacion.getDateCreation())
                        .estado(reservacion.getIdTypeReservation().getName())
                        .build())
                .toList();

        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<List<ReservacionDetalleResponse>> listaReservacionesDetalle(Integer idReservacion) {
        List<ReservacionDetalleResponse> respuesta = reservacionDetalleRepository.findByReservations_IdReservation(idReservacion)
                .stream().map(detalle ->
                        ReservacionDetalleResponse
                                .builder()
                                .tableId(detalle.getTableId().getIdTable())
                                .amountPeople(detalle.getAmountPeople())
                                .requirements(detalle.getRequirements())
                                .hourReservation(detalle.getHourReservation())
                                .dateReservation(detalle.getDateReservation())
                                .imagenMesa(detalle.getTableId().getImagen())
                                .build()
                ).toList();

        return ResponseEntity.ok(respuesta);
    }

    public ResponseEntity<List<ReservacionResponse>> listaReservaciones(String estado) {
        List<ReservacionResponse> responses = reservacionRepository.findAllByOrderByIdReservationDesc()
                .stream()
                .filter(reservacion ->
                        estado == null || estado.isBlank() || reservacion.getIdTypeReservation().getName().equals(estado))
                .map(reservacion -> ReservacionResponse.builder()
                        .idReservacion(reservacion.getIdReservation())
                        .namereservation(reservacion.getNameReservation())
                        .fechaCreacion(reservacion.getDateCreation())
                        .estado(reservacion.getIdTypeReservation().getName())
                        .build())
                .toList();

        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<String> aprobarReservaciones(Integer idReserva) {
        return reservacionRepository.findById(idReserva).map(reservacion -> {
            TypeReservation reservacionTipo = typeReservationRepository.findById(ConstansApi.CONFIRMADO).orElseThrow(()-> new GlobalException("No se encontro el tipo de reservacion", 404));
            reservacion.setIdTypeReservation(reservacionTipo);
            reservacionRepository.save(reservacion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reserva aprobada");
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<String> cancelarReservaciones(Integer idReserva) {
        return reservacionRepository.findById(idReserva).map(reservacion -> {
            TypeReservation reservacionTipo = typeReservationRepository.findById(ConstansApi.CANCELADO).orElseThrow(()-> new GlobalException("No se encontro el tipo de reservacion", 404));
            reservacion.setIdTypeReservation(reservacionTipo);
            reservacionRepository.save(reservacion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reserva Cancelada");
        }).orElse(ResponseEntity.notFound().build());
    }
}
