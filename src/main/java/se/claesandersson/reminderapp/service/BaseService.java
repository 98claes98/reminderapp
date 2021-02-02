/*
 * 
 */
package se.claesandersson.reminderapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import se.claesandersson.reminderapp.security.AccessManager;

public abstract class BaseService {
    
    @Autowired
    AccessManager accessManager;
}
