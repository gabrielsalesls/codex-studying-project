package dev.gabrielsales.user_service.wallet;

import dev.gabrielsales.user_service.user.UserType;

public record CreateWalletRequest(
        Long userId,
        UserType userType
) {
}
