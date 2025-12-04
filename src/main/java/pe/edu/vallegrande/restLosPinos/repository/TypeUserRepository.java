package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.TypeUser;

import java.util.Optional;

@Repository
public interface TypeUserRepository extends JpaRepository<TypeUser, Integer> {
    Optional<TypeUser> findByName(String name);
}
