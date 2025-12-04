package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.TypeReservation;

import java.util.List;


@Repository
public interface TypeReservationRepository extends JpaRepository<TypeReservation, Integer> {

    List<TypeReservation> findByState(String activo);
}
