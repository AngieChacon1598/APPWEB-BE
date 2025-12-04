package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.MesaRestaurante;

import java.util.List;
import java.util.Optional;


@Repository
public interface MesaRestauranteRepository extends JpaRepository<MesaRestaurante, Integer> {

    List<MesaRestaurante> findByNumberTable(Integer numberTable);

    List<MesaRestaurante> findByState(String estado);
}
