/*
 * 
 */
package se.claesandersson.reminderapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.claesandersson.reminderapp.domain.Reminder;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findAllByUserId(long userId);
    void deleteAllByUserId(long userId);
}
