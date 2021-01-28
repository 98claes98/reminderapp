/*
 * 
 */
package se.claesandersson.reminderapp.security;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.claesandersson.reminderapp.domain.User;
import se.claesandersson.reminderapp.repository.UserRepository;

@Service
public class AccessManager {

    @Autowired
    private UserRepository userRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public AuthenticationSuccess validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User u = user.get();
            if (u.getPassword().equals(password)) {
                u.setApiKey(generateNewKey());
                u.setLastRequest(new Timestamp(Instant.now().getEpochSecond() * 1000));
                userRepository.save(u);
                return new AuthenticationSuccess(u.getId(), u.getApiKey());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public User validateKey(String key) {
        Optional<User> user = userRepository.findByApiKey(key);
        if (user.isPresent()) {
            User u = user.get();
            long timeNow = Instant.now().getEpochSecond() * 1000;
            if (u.getLastRequest().getTime() > timeNow - 60 * 30 * 1000) {
                u.setLastRequest(new Timestamp(timeNow));
                userRepository.save(u);
                return u;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String generateNewKey() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
