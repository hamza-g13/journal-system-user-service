package se.kth.journalsystem.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.kth.journalsystem.auth.dto.AuthResponse;
import se.kth.journalsystem.auth.dto.LoginRequest;
import se.kth.journalsystem.auth.model.User;
import se.kth.journalsystem.auth.security.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        User user = userService.findByUsername(request.username());

        // Validate password with BCrypt
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().toString(),
                user.getId());
    }

    public AuthResponse register(se.kth.journalsystem.auth.dto.RegisterRequest request) {
        se.kth.journalsystem.auth.dto.UserResponse user = userService.registerUser(request);

        // Auto-login after register
        User userEntity = userService.findByUsername(user.username());
        String token = jwtUtil.generateToken(userEntity);

        return new AuthResponse(
                token,
                userEntity.getUsername(),
                userEntity.getRole().toString(),
                userEntity.getId());
    }
}
