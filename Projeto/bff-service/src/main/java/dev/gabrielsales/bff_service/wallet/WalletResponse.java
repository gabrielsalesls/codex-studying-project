package dev.gabrielsales.bff_service.wallet;

import java.math.BigDecimal;

public record WalletResponse(
        Long userId,
        BigDecimal balance
) {
}
