package dev.gabrielsales.wallet_service.wallet.api;

import java.math.BigDecimal;

public record WalletResponse(
        Long userId,
        BigDecimal balance
) {
}
