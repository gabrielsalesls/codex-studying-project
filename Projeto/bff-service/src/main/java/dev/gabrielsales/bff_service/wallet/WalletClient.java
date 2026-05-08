package dev.gabrielsales.bff_service.wallet;

import dev.gabrielsales.bff_service.common.exception.DownstreamServiceException;
import dev.gabrielsales.bff_service.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class WalletClient {

    private final RestClient restClient;

    public WalletClient(@Value("${wallet-service.base-url}") String walletServiceBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(walletServiceBaseUrl)
                .build();
    }

    public WalletResponse findByUserId(Long userId) {
        try {
            var response = restClient.get()
                    .uri("/wallet/{userId}", userId)
                    .retrieve()
                    .body(WalletResponse.class);

            if (response == null) {
                throw new DownstreamServiceException(
                        "Failed to retrieve wallet for user with id %s".formatted(userId),
                        null
                );
            }

            return response;
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException("Wallet not found for user with id %s".formatted(userId));
        } catch (RestClientException exception) {
            throw new DownstreamServiceException(
                    "Failed to retrieve wallet for user with id %s".formatted(userId),
                    exception
            );
        }
    }
}
