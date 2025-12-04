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
@Table(name = "type_users", schema = "DEVELOPER_BEINGOLEA")

public class TypeUser {
    @Id
    @Column(name = "id_type_users")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTypeUsers;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private String state;
}
