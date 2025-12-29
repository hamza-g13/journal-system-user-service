package se.kth.journalsystem.auth.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.kth.journalsystem.auth.dto.RegisterRequest;
import se.kth.journalsystem.auth.dto.UpdateUserRequest;
import se.kth.journalsystem.auth.dto.UserResponse;
import se.kth.journalsystem.auth.model.User;
import se.kth.journalsystem.auth.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.findByUsername(username);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody RegisterRequest request) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(userService.createUser(request, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(userService.getAllUsers(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(userService.updateUser(id, request, currentUser));
    }
}
