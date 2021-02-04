/*
 * 
 */
package se.claesandersson.reminderapp.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.claesandersson.reminderapp.domain.Reminder;
import se.claesandersson.reminderapp.domain.User;
import se.claesandersson.reminderapp.repository.ReminderRepository;

@Service
public class ReminderService extends BaseService {

    @Autowired
    private ReminderRepository reminderRepository;

    public ResponseEntity getRemindersByUserId(long userId, String key) {
        User u = accessManager.validateKey(key);
        if (u == null || !u.getId().equals(userId)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else {
            List<Reminder> reminders = reminderRepository.findAllByUserIdOrderByDatetimeAsc(u.getId());
            if (reminders.isEmpty()) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity(reminders, HttpStatus.OK);
            }
        }
    }

    public ResponseEntity addReminder(Reminder reminder, String key) {
        User u = accessManager.validateKey(key);
        if (u == null || !u.getId().equals(reminder.getUserId())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else {
            if (reminder.getTitle() == null || reminder.getTitle().equals("") || reminder.getTitle().length() > 20 || reminder.getFinished() == null || (reminder.getDescription() != null && reminder.getDescription().length() > 100)) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            } else {
                reminderRepository.save(reminder);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
    }

    public ResponseEntity editReminder(Reminder reminder, String key) {
        User u = accessManager.validateKey(key);
        if (u == null || !u.getId().equals(reminder.getUserId())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else {
            Optional<Reminder> r = reminderRepository.findById(reminder.getId());
            if (!r.isPresent()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                if (reminder.getTitle() != null) {
                    if (reminder.getTitle().equals("")) {
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    } else {
                        r.get().setTitle(reminder.getTitle());
                    }
                }
                if (reminder.getDescription() != null) {
                    if (reminder.getDescription().length() > 100) {
                        r.get().setDescription(reminder.getDescription());
                    } else {
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                }
                if (reminder.getDatetime() != null) {
                    r.get().setDatetime(reminder.getDatetime());
                }
                if (reminder.getFinished() != null) {
                    r.get().setFinished(reminder.getFinished());
                }
            }
            reminderRepository.save(r.get());
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    public ResponseEntity deleteReminder(long id, String key) {
        User u = accessManager.validateKey(key);
        Optional<Reminder> r = reminderRepository.findById(id);
        if (!r.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else if (u == null || !u.getId().equals(r.get().getUserId())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else {
            if (!reminderRepository.existsById(id)) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                reminderRepository.deleteById(id);
                return new ResponseEntity(HttpStatus.OK);

            }
        }
    }
}
