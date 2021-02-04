/*
 * 
 */
package se.claesandersson.reminderapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import se.claesandersson.reminderapp.domain.Reminder;

@Transactional
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findAllByUserIdOrderByDatetimeAsc(long userId);
    void deleteAllByUserId(long userId);
}
