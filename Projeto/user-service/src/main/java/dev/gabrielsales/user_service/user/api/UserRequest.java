package dev.gabrielsales.user_service.user.api;

import dev.gabrielsales.user_service.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "must not be blank")
        String name,
        @NotBlank(message = "must not be blank")
        @Size(min = 11, max = 14, message = "must have between 11 and 14 characters")
        String cpf,
        @NotBlank(message = "must not be blank")
        @Email(message = "must be a valid email")
        String email,
        @NotBlank(message = "must not be blank")
        String password,
        @NotNull(message = "must not be null")
        UserType type
) {
}
