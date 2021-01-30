/*
 * 
 */
package se.claesandersson.reminderapp.controller;

import java.util.Optional;
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
import se.claesandersson.reminderapp.mail.SendMail;
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

    @PostMapping("/users/check")
    public ResponseEntity checkEmail(@RequestBody User user) {
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                return new ResponseEntity(HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/recover")
    public ResponseEntity recover(@RequestBody User user) {
        try {
            Optional<User> u = userRepository.findByEmail(user.getEmail());
            if (!u.isPresent()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                Runnable r = () -> {
                    SendMail.sendMail(u.get().getEmail(), "Account recovery", "Your account credentials:\r\nEmail: " + u.get().getEmail() + "\r\nPassword: " + u.get().getPassword());
                };
                new Thread(r).start();
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/authenticate")
    public ResponseEntity authenticate(@RequestBody User user) {
        try {
            AuthenticationSuccess auth = accessManager.validateUser(user.getEmail(), user.getPassword());
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
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
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
                    if (accessManager.validateUser(user.getEmail(), user.getPassword()) == null) {
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
