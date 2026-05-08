package dev.gabrielsales.bff_service.summary;

import dev.gabrielsales.bff_service.summary.api.UserSummaryResponse;
import dev.gabrielsales.bff_service.user.UserClient;
import dev.gabrielsales.bff_service.wallet.WalletClient;
import org.springframework.stereotype.Service;

@Service
public class UserSummaryService {

    private final UserClient userClient;
    private final WalletClient walletClient;

    public UserSummaryService(UserClient userClient, WalletClient walletClient) {
        this.userClient = userClient;
        this.walletClient = walletClient;
    }

    public UserSummaryResponse findByUserId(Long userId) {
        var user = userClient.findById(userId);
        var wallet = walletClient.findByUserId(userId);

        return new UserSummaryResponse(
                user.id(),
                user.name(),
                user.type(),
                wallet.balance()
        );
    }
}
