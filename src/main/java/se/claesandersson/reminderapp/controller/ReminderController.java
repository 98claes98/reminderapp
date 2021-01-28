/*
 * 
 */
package se.claesandersson.reminderapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.claesandersson.reminderapp.domain.Reminder;
import se.claesandersson.reminderapp.domain.User;
import se.claesandersson.reminderapp.repository.ReminderRepository;

@CrossOrigin
@RestController
public class ReminderController extends AbstractController {
    
    @Autowired
    ReminderRepository reminderRepository;

    @GetMapping("/reminders/{userid}")
    public ResponseEntity getByUserId(@PathVariable("userid") long userId, @RequestParam String key) {
        try {
            User u = accessManager.validateKey(key);
            if (u == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                if (u.getId() != userId) {
                    return new ResponseEntity(HttpStatus.UNAUTHORIZED);
                } else {
                    List<Reminder> reminders = reminderRepository.findAllByUserId(userId);
                    if (reminders.isEmpty()) {
                        return new ResponseEntity(HttpStatus.NO_CONTENT);
                    } else {
                        return new ResponseEntity(reminders, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reminders")
    public ResponseEntity post(@RequestBody Reminder reminder, @RequestParam String key) {
        try {
            User u = accessManager.validateKey(key);
            if (u == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                if (u.getId() != reminder.getUserId()) {
                    return new ResponseEntity(HttpStatus.UNAUTHORIZED);
                } else {
                    reminderRepository.save(reminder);
                    return new ResponseEntity(HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/reminders")
    public ResponseEntity put(@RequestBody Reminder reminder, @RequestParam String key) {
        try {
            User u = accessManager.validateKey(key);
            if (u == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                if (u.getId() != reminder.getUserId()) {
                    return new ResponseEntity(HttpStatus.UNAUTHORIZED);
                } else {
                    if (!reminderRepository.existsById(reminder.getId())) {
                        return new ResponseEntity(HttpStatus.NOT_FOUND);
                    } else {
                        reminderRepository.save(reminder);
                        return new ResponseEntity(HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/reminders/{id}")
    public ResponseEntity delete(@PathVariable("id") long id, @RequestParam String key) {
        try {
            User u = accessManager.validateKey(key);
            if (u == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                if (!reminderRepository.existsById(id)) {
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                } else {
                    Reminder r = reminderRepository.getOne(id);
                    if (u.getId() != r.getUserId()) {
                        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
                    } else {
                        reminderRepository.deleteById(id);
                        return new ResponseEntity(HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
