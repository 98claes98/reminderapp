/*
 * 
 */
package se.claesandersson.reminderapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.claesandersson.reminderapp.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    Optional<User> findByApiKey(String key);
    boolean existsByEmail(String email);
}
