package se.kth.journalsystem.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.kth.journalsystem.auth.dto.RegisterRequest;
import se.kth.journalsystem.auth.dto.UpdateUserRequest;
import se.kth.journalsystem.auth.dto.UserResponse;
import se.kth.journalsystem.auth.model.User;
import se.kth.journalsystem.auth.model.UserRole;
import se.kth.journalsystem.auth.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorizationService authorizationService;

    public UserResponse createUser(RegisterRequest request, User currentUser) {
        if (!authorizationService.canCreateUser(currentUser)) {
            throw new RuntimeException("Only admins can create users");
        }
        return registerUser(request);
    }

    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        user = userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    public List<UserResponse> getAllUsers(User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admins can view all users");
        }

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getRole()))
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = findById(id);
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    public UserResponse getUserByUsername(String username) {
        User user = findByUsername(username);
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest request, User currentUser) {
        User userToUpdate = findById(userId);

        if (currentUser.getRole() == UserRole.ADMIN) {
            if (userToUpdate.getRole() == UserRole.ADMIN && !currentUser.getId().equals(userId)) {
                throw new RuntimeException("Admins cannot manage other admins");
            }
        } else {
            if (!currentUser.getId().equals(userId)) {
                throw new RuntimeException("You can only update your own account");
            }
        }

        if (request.username() != null) {
            userToUpdate.setUsername(request.username());
        }
        if (request.password() != null) {
            userToUpdate.setPassword(passwordEncoder.encode(request.password()));
        }

        userToUpdate = userRepository.save(userToUpdate);
        return new UserResponse(userToUpdate.getId(), userToUpdate.getUsername(), userToUpdate.getRole());
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
