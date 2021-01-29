/*
 * 
 */
package se.claesandersson.reminderapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.claesandersson.reminderapp.domain.User;
import se.claesandersson.reminderapp.repository.ReminderRepository;
import se.claesandersson.reminderapp.repository.UserRepository;
import se.claesandersson.reminderapp.security.AuthenticationSuccess;

@CrossOrigin
@RestController
public class UserController extends AbstractController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReminderRepository reminderRepository;

    @PostMapping("/users/authenticate")
    public ResponseEntity authenticate(@RequestBody User user) {
        try {
            AuthenticationSuccess auth = accessManager.validateUser(user.getUsername(), user.getPassword());
            if (auth == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity(auth, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ResponseEntity post(@RequestBody User user) {
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return new ResponseEntity(HttpStatus.CONFLICT);
            } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new ResponseEntity(HttpStatus.CONFLICT);
            } else {
                userRepository.save(user);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users")
    public ResponseEntity put(@RequestBody User user, @RequestParam String key) {
        try {
            User u = accessManager.validateKey(key);
            if (u == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                if (u.getId() != user.getId()) {
                    return new ResponseEntity(HttpStatus.UNAUTHORIZED);
                } else {
                    userRepository.save(user);
                    return new ResponseEntity(HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity delete(@RequestBody User user, @RequestParam String key) {
        try {
            User u = accessManager.validateKey(key);
            if (u == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                if (u.getId() != user.getId()) {
                    return new ResponseEntity(HttpStatus.UNAUTHORIZED);
                } else {
                    if (accessManager.validateUser(user.getUsername(), user.getPassword()) == null) {
                        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
                    } else {
                        reminderRepository.deleteAllByUserId(user.getId());
                        userRepository.deleteById(user.getId());
                        return new ResponseEntity(HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
