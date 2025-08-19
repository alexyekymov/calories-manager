package dev.overlax.caloriesmanager.repository;

import dev.overlax.caloriesmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.meals WHERE u.id = :id")
    Optional<User> findByIdWithMeals(int id);
}
