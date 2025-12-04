package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.dto.ApiResponse;
import pe.edu.vallegrande.restLosPinos.dto.response.ReservacionResponse;
import pe.edu.vallegrande.restLosPinos.model.Reservacion;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ReservacionRepository extends JpaRepository<Reservacion, Integer> {
    List<Reservacion> findByRestaurantUser_IdUserOrderByIdReservationDesc(Integer idUsuario);

    List<Reservacion> findAllByOrderByIdReservationDesc();
}