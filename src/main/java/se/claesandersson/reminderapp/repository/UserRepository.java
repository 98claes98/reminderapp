/*
 * 
 */
package se.claesandersson.reminderapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.claesandersson.reminderapp.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByUsername(String username);
}
