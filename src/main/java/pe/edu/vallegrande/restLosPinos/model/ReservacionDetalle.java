package pe.edu.vallegrande.restLosPinos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "reservation_detail", schema = "DEVELOPER_BEINGOLEA")
public class ReservacionDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation_details")
    private Integer idReservationDetails;

    @Column(name = "amount_people")
    private Integer amountPeople;

    @Column(name = "hour_reservation")
    private LocalTime hourReservation;

    @Column(name = "date_reservation")
    private LocalDate dateReservation;

    @Lob
    @Column(name = "requirements")
    private String requirements;

    @OneToOne
    @JoinColumn(name = "table_id" , referencedColumnName = "id_table")
    private MesaRestaurante tableId;

    @ManyToOne
    @JoinColumn(name = "reservations", referencedColumnName = "id_reservation")
    private Reservacion reservations;
}
