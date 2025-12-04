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
@Table(name = "restaurant_tables", schema = "DEVELOPER_BEINGOLEA")
public class MesaRestaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_table")
    private Integer idTable;

    @Column(name = "number_table")
    private Integer numberTable;

    @Column(name = "ability")
    private Integer ability;

    @Column(name = "description")
    private String descripcion;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "state")
    private String state;

}
