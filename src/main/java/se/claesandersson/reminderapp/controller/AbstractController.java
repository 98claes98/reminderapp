/*
 * 
 */
package se.claesandersson.reminderapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import se.claesandersson.reminderapp.security.AccessManager;

public abstract class AbstractController {
    
    @Autowired
    AccessManager accessManager;
}
