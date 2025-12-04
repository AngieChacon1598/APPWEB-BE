package pe.edu.vallegrande.restLosPinos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "reservations", schema = "DEVELOPER_BEINGOLEA")
public class Reservacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Integer idReservation;

    @Column(name = "name_reservation")
    private String nameReservation;

    @Column(name = "state")
    private String state;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @OneToOne
    @JoinColumn(name = "id_type_reservation", referencedColumnName = "id_type_reservation")
    private TypeReservation idTypeReservation;

    @ManyToOne
    @JoinColumn(name = "restaurant_user", referencedColumnName = "id_user")
    private RestaurantUser restaurantUser;
}
