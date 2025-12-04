package pe.edu.vallegrande.restLosPinos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "type_state_reservation", schema = "DEVELOPER_BEINGOLEA")
public class TypeReservation {

    @Id
    @Column(name = "id_type_reservation")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTypeReservation;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private String state;
}
