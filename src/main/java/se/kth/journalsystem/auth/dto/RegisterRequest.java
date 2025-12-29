package se.kth.journalsystem.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import se.kth.journalsystem.auth.model.UserRole;

public record RegisterRequest(
        @NotBlank(message = "Username is required") @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String username,

        @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,

        @NotNull(message = "Role is required") UserRole role) {
}
