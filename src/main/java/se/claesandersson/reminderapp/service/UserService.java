/*
 * 
 */
package se.claesandersson.reminderapp.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.claesandersson.reminderapp.domain.User;
import se.claesandersson.reminderapp.mail.SendMail;
import se.claesandersson.reminderapp.repository.ReminderRepository;
import se.claesandersson.reminderapp.repository.UserRepository;
import se.claesandersson.reminderapp.security.AuthenticationSuccess;

@Service
public class UserService extends BaseService {

    private final String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReminderRepository reminderRepository;

    public ResponseEntity checkEmail(User user) {
        if (user.getEmail() == null || !user.getEmail().matches(emailRegex)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else if (userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    public ResponseEntity recoverAccount(User user) {
        if (user.getEmail() == null || !user.getEmail().matches(emailRegex)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
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
        }
    }

    public ResponseEntity authenticateUser(User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            AuthenticationSuccess auth = accessManager.validateUser(user.getEmail(), user.getPassword());
            if (auth == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity(auth, HttpStatus.OK);
            }
        }
    }

    public ResponseEntity addUser(User user) {
        if (user.getEmail() == null || !user.getEmail().matches(emailRegex) || user.getPassword() == null || user.getPassword().length() < 10) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } else {
            userRepository.save(user);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    public ResponseEntity editUser(User user, String key) {
        User u = accessManager.validateKey(key);
        if (u == null || !u.getId().equals(user.getId())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else {
            if (user.getEmail() != null) {
                if (!user.getEmail().matches(emailRegex)) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                } else {
                    u.setEmail(user.getEmail());
                }
            }
            if (user.getPassword() != null) {
                if (user.getPassword().length() < 10) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                } else {
                    u.setPassword(user.getPassword());
                }
            }
            userRepository.save(u);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    public ResponseEntity deleteUser(long id, String key) {
        User u = accessManager.validateKey(key);
        if (u == null || !u.getId().equals(id)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else {
            reminderRepository.deleteAllByUserId(u.getId());
            userRepository.deleteById(u.getId());
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
