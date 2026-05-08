package dev.gabrielsales.user_service.user.api;

import dev.gabrielsales.user_service.user.UserType;

public record CreateUserResponse(
        Long id,
        String name,
        UserType type,
        WalletResponse wallet
) {
}
