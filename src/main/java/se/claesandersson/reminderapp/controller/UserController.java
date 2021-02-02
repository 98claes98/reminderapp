/*
 * 
 */
package se.claesandersson.reminderapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.claesandersson.reminderapp.domain.User;
import se.claesandersson.reminderapp.service.UserService;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/check")
    public ResponseEntity checkEmail(@RequestBody User user) {
        
        return userService.checkEmail(user);
    }

    @PostMapping("/users/recover")
    public ResponseEntity recover(@RequestBody User user) {
        
        return userService.recoverAccount(user);
    }

    @PostMapping("/users/authenticate")
    public ResponseEntity authenticate(@RequestBody User user) {
        
        return userService.authenticateUser(user);
    }

    @PostMapping("/users")
    public ResponseEntity add(@RequestBody User user) {
        
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public ResponseEntity edit(@RequestBody User user) {
        
        return userService.editUser(user);
    }

    @DeleteMapping("/users")
    public ResponseEntity delete(@RequestBody User user) {
        
        return userService.deleteUser(user);
    }
}
