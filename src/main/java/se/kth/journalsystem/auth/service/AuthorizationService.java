package se.kth.journalsystem.auth.service;

import org.springframework.stereotype.Service;
import se.kth.journalsystem.auth.model.User;
import se.kth.journalsystem.auth.model.UserRole;

@Service
public class AuthorizationService {

    // === USERS ===
    public boolean canCreateUser(User currentUser) {
        return currentUser.getRole() == UserRole.ADMIN;
    }
}
