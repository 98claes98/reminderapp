/*
 * 
 */
package se.claesandersson.reminderapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import se.claesandersson.reminderapp.service.ReminderService;

@CrossOrigin
@RestController
public class ReminderController {
    
    @Autowired
    private ReminderService reminderService;

    @GetMapping("/reminders/{userid}")
    public ResponseEntity getByUserId(@PathVariable long userid, @RequestParam String key) {
        
        return reminderService.getRemindersByUserId(userid, key);
    }

    @PostMapping("/reminders")
    public ResponseEntity add(@RequestBody Reminder reminder, @RequestParam String key) {
        
        return reminderService.addReminder(reminder, key);
    }

    @PutMapping("/reminders")
    public ResponseEntity put(@RequestBody Reminder reminder, @RequestParam String key) {
        
        return reminderService.editReminder(reminder, key);
    }

    @DeleteMapping("/reminders/{id}")
    public ResponseEntity delete(@PathVariable long id, @RequestParam String key) {
        
        return reminderService.deleteReminder(id, key);
    }
}
