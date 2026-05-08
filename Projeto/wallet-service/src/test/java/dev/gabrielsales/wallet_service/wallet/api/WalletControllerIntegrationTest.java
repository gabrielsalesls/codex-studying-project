package dev.gabrielsales.wallet_service.wallet.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gabrielsales.wallet_service.wallet.WalletRepository;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletControllerIntegrationTest {

    private static final String WALLET_PATH = "/wallet";

    @Autowired
    private WalletRepository walletRepository;

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateWallet() throws Exception {
        walletRepository.deleteAll();

        var payload = """
                {
                  "userId": 1,
                  "userType": "COMMON"
                }
                """;

        var response = sendPost(payload);
        var body = readBody(response);

        assertEquals(201, response.statusCode());
        assertEquals("/wallet/1", response.headers().firstValue("Location").orElseThrow());
        assertTrue(body.get("walletId").asText().startsWith("wallet-"));
        assertEquals(1L, body.get("userId").asLong());
        assertEquals(0, body.get("balance").decimalValue().signum());
    }

    @Test
    void shouldReturnConflictWhenWalletAlreadyExistsForUser() throws Exception {
        walletRepository.deleteAll();

        var payload = """
                {
                  "userId": 1,
                  "userType": "COMMON"
                }
                """;

        sendPost(payload);

        var response = sendPost(payload);
        var body = readBody(response);

        assertEquals(409, response.statusCode());
        assertEquals(409, body.get("status").asInt());
        assertEquals("Conflict", body.get("error").asText());
        assertEquals("Wallet already exists for user with id 1", body.get("message").asText());
        assertEquals(WALLET_PATH, body.get("path").asText());
    }

    private HttpResponse<String> sendPost(String payload) throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:%s%s".formatted(port, WALLET_PATH)))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private JsonNode readBody(HttpResponse<String> response) throws Exception {
        return objectMapper.readTree(response.body());
    }
}
