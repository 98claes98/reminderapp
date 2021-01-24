/*
 * 
 */
package se.claesandersson.reminderapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.claesandersson.reminderapp.domain.User;
import se.claesandersson.reminderapp.repository.UserRepository;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity post(@RequestBody User user) {
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                return new ResponseEntity(HttpStatus.CONFLICT);
            } else {
                User u = new User(user.getUsername(), user.getPassword());
                userRepository.save(u);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity post(@PathVariable("id") long id, @RequestBody User user) {
        try {
            if (!userRepository.existsById(id)) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                User u = userRepository.getOne(id);
                u.setPassword(user.getPassword());
                userRepository.save(u);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity delete(@PathVariable("id") long id) {
        try {
            if (!userRepository.existsById(id)) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                userRepository.deleteById(id);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
