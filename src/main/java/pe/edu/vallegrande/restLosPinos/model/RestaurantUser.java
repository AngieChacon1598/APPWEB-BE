package pe.edu.vallegrande.restLosPinos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "restaurant_user", schema = "DEVELOPER_BEINGOLEA")

public class RestaurantUser implements UserDetails {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "names")
    private String names;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Lob
    @Column(name = "address")
    private String address;

    @Column(name = "email" , unique = true)
    private String email;

    @Column(name = "type_document")
    private String typeDocument;

    @Column(name = "number_document" , unique = true)
    private String numberDocument;

    @Column(name = "state")
    private String state;

    @OneToOne
    @JoinColumn(name = "type_users_id_type_users" , referencedColumnName = "id_type_users")
    private TypeUser typeUsersIdTypeUsers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (typeUsersIdTypeUsers == null || typeUsersIdTypeUsers.getName() == null) {
            return List.of(); // Retorna lista vacía si no hay rol
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + typeUsersIdTypeUsers.getName()));
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
