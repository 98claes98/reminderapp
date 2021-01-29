/*
 * 
 */
package se.claesandersson.reminderapp.domain;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String apiKey;
    private Timestamp lastRequest;
    private String email;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public long getId(){
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Timestamp getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Timestamp lastRequest) {
        this.lastRequest = lastRequest;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
