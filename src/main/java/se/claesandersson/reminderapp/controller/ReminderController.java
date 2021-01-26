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
import org.springframework.web.bind.annotation.RestController;
import se.claesandersson.reminderapp.domain.Reminder;
import se.claesandersson.reminderapp.repository.ReminderRepository;

@CrossOrigin
@RestController
public class ReminderController {

    @Autowired
    ReminderRepository reminderRepository;

    @GetMapping("/reminders/{userid}")
    public ResponseEntity getByUserId(@PathVariable("userid") long userId) {
        try {
            List<Reminder> reminders = reminderRepository.findAllByUserId(userId);
            if (reminders.isEmpty()) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity(reminders, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reminders")
    public ResponseEntity post(@RequestBody Reminder reminder) {
        try {
            reminderRepository.save(reminder);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/reminders")
    public ResponseEntity put(@RequestBody Reminder reminder) {
        try {
            if (!reminderRepository.existsById(reminder.getId())) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                reminderRepository.save(reminder);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/reminders/{id}")
    public ResponseEntity delete(@PathVariable("id") long id) {
        try {
            if (!reminderRepository.existsById(id)) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                reminderRepository.deleteById(id);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
