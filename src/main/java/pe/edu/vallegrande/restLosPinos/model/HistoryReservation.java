package pe.edu.vallegrande.restLosPinos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "history_reservation", schema = "DEVELOPER_BEINGOLEA")
public class HistoryReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_history")
    private Integer idHistory;

    @Column(name = "table_restaurant")
    private Integer tableRestaurant;

    @Column(name = "hour_reservation")
    private LocalDateTime hourReservation;

    @Column(name = "date_reservation")
    private LocalDate dateReservation;

    @Column(name = "estado")
    private String estado;

}
