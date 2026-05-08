package dev.gabrielsales.bff_service.summary.api;

import dev.gabrielsales.bff_service.user.UserType;
import java.math.BigDecimal;

public record UserSummaryResponse(
        Long userId,
        String name,
        UserType type,
        BigDecimal balance
) {
}
