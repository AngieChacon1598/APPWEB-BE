package pe.edu.vallegrande.restLosPinos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category", schema = "DEVELOPER_ANGI")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    // En la base de datos es CHAR(1)
    @Column(name = "state", columnDefinition = "CHAR(1)")
    @Builder.Default
    private String state = "1"; // 1 = activo, 0 = inactivo

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (state == null) {
            state = "1";
        }
    }
}
