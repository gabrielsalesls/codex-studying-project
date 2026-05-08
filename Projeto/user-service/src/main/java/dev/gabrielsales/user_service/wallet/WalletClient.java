package dev.gabrielsales.user_service.wallet;

import dev.gabrielsales.user_service.common.exception.DownstreamServiceException;
import dev.gabrielsales.user_service.user.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class WalletClient {

    private final RestClient restClient;

    public WalletClient(
            @Value("${wallet-service.base-url}") String walletServiceBaseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(walletServiceBaseUrl)
                .build();
    }

    public void createWallet(Long userId, UserType userType) {
        try {
            restClient.post()
                    .uri("/wallet")
                    .body(new CreateWalletRequest(userId, userType))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException exception) {
            throw new DownstreamServiceException(
                    "Failed to create wallet for user with id %s".formatted(userId),
                    exception
            );
        }
    }
}
