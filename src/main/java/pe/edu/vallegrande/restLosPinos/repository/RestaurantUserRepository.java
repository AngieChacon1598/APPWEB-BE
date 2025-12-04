package pe.edu.vallegrande.restLosPinos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.restLosPinos.model.RestaurantUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Integer> {
    Optional<RestaurantUser> findByUserName(String username);
    
    @Query("SELECT u FROM RestaurantUser u LEFT JOIN FETCH u.typeUsersIdTypeUsers WHERE u.userName = :username")
    Optional<RestaurantUser> findByUserNameWithRole(@Param("username") String username);

    Page<RestaurantUser> findByState (String estado , Pageable pageable);

    @Query("""
       SELECT u FROM RestaurantUser u 
       WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :search, '%'))
          OR LOWER(u.names) LIKE LOWER(CONCAT('%', :search, '%'))
          OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
       """)
    Page<RestaurantUser> searchByMultipleFields(@Param("search") String search, Pageable pageable);


}
