package dev.gabrielsales.bff_service.user;

import dev.gabrielsales.bff_service.common.exception.DownstreamServiceException;
import dev.gabrielsales.bff_service.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${user-service.base-url}") String userServiceBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(userServiceBaseUrl)
                .build();
    }

    public UserResponse findById(Long userId) {
        try {
            var response = restClient.get()
                    .uri("/api/v1/users/{id}", userId)
                    .retrieve()
                    .body(UserResponse.class);

            if (response == null) {
                throw new DownstreamServiceException(
                        "Failed to retrieve user with id %s".formatted(userId),
                        null
                );
            }

            return response;
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException("User with id %s not found".formatted(userId));
        } catch (RestClientException exception) {
            throw new DownstreamServiceException(
                    "Failed to retrieve user with id %s".formatted(userId),
                    exception
            );
        }
    }
}
