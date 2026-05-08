package dev.gabrielsales.wallet_service.wallet.api;

import dev.gabrielsales.wallet_service.wallet.UserType;
import jakarta.validation.constraints.NotNull;

public record CreateWalletRequest(
        @NotNull(message = "must not be null")
        Long userId,

        @NotNull(message = "must not be null")
        UserType userType
) {
}
