package dev.gabrielsales.user_service.wallet;

public record WalletCreatedResponse(
        String walletId,
        Long userId
) {
}
