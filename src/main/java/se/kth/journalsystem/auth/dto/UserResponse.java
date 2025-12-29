package se.kth.journalsystem.auth.dto;

import se.kth.journalsystem.auth.model.UserRole;

public record UserResponse(
        Long id,
        String username,
        UserRole role) {
}
