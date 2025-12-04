package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.dto.response.HistoryReservationResponse;
import pe.edu.vallegrande.restLosPinos.model.HistoryReservation;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoryReservationRepository extends JpaRepository<HistoryReservation, Integer> {

    @Query(value = """
           SELECT
           ESTADO,
           HOUR_RESERVATION,
           DATE_RESERVATION
           FROM DEVELOPER_BEINGOLEA.HISTORY_RESERVATION
           WHERE
           TABLE_RESTAURANT = :MESA
           AND TRUNC(DATE_RESERVATION) = TO_DATE(:FECHA, 'dd/mm/yyyy')
           """, nativeQuery = true)
    List<HistoryReservationResponse> listarReservaciones(
            @Param("FECHA") String fecha,
            @Param("MESA") Integer mesa);


}
