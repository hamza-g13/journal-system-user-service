package se.kth.journalsystem.auth.dto;

public record AuthResponse(
        String token,
        String username,
        String role,
        Long userId) {
}
