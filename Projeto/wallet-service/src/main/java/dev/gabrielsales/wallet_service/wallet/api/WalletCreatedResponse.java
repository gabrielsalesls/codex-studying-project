package dev.gabrielsales.wallet_service.wallet.api;

import java.math.BigDecimal;

public record WalletCreatedResponse(
        String walletId,
        Long userId,
        BigDecimal balance
) {
}
