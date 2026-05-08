package dev.gabrielsales.bff_service.user;

public record UserResponse(
        Long id,
        String name,
        UserType type
) {
}
