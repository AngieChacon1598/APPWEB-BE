package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.dto.response.ReservacionDetalleResponse;
import pe.edu.vallegrande.restLosPinos.model.ReservacionDetalle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservacionDetalleRepository extends JpaRepository<ReservacionDetalle, Integer> {
    List<ReservacionDetalle> findByTableId_IdTableAndDateReservationAndHourReservationBetween(Integer idMesa, LocalDate fecha, LocalTime inicio, LocalTime fin);

    List<ReservacionDetalle> findByReservations_IdReservation(Integer idReservacion);
}
