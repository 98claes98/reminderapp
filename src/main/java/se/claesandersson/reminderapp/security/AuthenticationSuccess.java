/*
 * 
 */
package se.claesandersson.reminderapp.security;

public class AuthenticationSuccess {
    
    private long userId;
    private String key;

    public AuthenticationSuccess(long userId, String key) {
        this.userId = userId;
        this.key = key;
    }
    
    public long getUserId(){
        return userId;
    }
    
    public void setUserId(long userId){
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    
}
