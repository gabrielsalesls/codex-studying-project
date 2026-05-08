package dev.gabrielsales.user_service.user.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dev.gabrielsales.user_service.user.UserRepository;
import java.io.IOException;
import java.net.URI;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    private static final String USERS_PATH = "/api/v1/users";
    private static final HttpServer walletServiceServer = createWalletServiceServer();
    private static final AtomicInteger walletServiceStatus = new AtomicInteger(201);
    private static final AtomicInteger walletCreateRequestCount = new AtomicInteger();
    private static final AtomicReference<String> walletServiceResponse = new AtomicReference(
            """
            {
              "walletId": "wallet-123",
              "userId": 1,
              "balance": 0.0
            }
            """
    );
    private static final AtomicReference<String> walletCreateRequestBody = new AtomicReference<>();

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "wallet-service.base-url",
                () -> "http://localhost:%s".formatted(walletServiceServer.getAddress().getPort())
        );
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        walletServiceStatus.set(201);
        walletServiceResponse.set(
                """
                {
                  "walletId": "wallet-123",
                  "userId": 1,
                  "balance": 0.0
                }
                """
        );
        walletCreateRequestBody.set(null);
        walletCreateRequestCount.set(0);
    }

    @AfterAll
    static void stopServer() {
        walletServiceServer.stop(0);
    }

    @Test
    void shouldCreateUser() throws Exception {
        var payload = """
                {
                  "name": "Maria",
                  "cpf": "12345678901",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "COMMON"
                }
                """;

        var response = sendRequest(USERS_PATH, "POST", payload);
        var body = readBody(response);

        var createdId = body.get("id").asLong();

        assertEquals(201, response.statusCode());
        assertEquals("%s/%s".formatted(USERS_PATH, createdId), response.headers().firstValue("Location").orElseThrow());
        assertEquals(createdId, body.get("id").asLong());
        assertEquals("Maria", body.get("name").asText());
        assertEquals("COMMON", body.get("type").asText());

        var walletRequest = objectMapper.readTree(walletCreateRequestBody.get());
        assertEquals(createdId, walletRequest.get("userId").asLong());
        assertEquals("COMMON", walletRequest.get("userType").asText());
        assertEquals(1, walletCreateRequestCount.get());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        var payload = """
                {
                  "name": "Maria",
                  "cpf": "12345678901",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "COMMON"
                }
                """;

        sendRequest(USERS_PATH, "POST", payload);

        var duplicateEmailPayload = """
                {
                  "name": "Joao",
                  "cpf": "98765432100",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "MERCHANT"
                }
                """;

        var response = sendRequest(USERS_PATH, "POST", duplicateEmailPayload);
        var body = readBody(response);

        assertEquals(409, response.statusCode());
        assertEquals(409, body.get("status").asInt());
        assertEquals("Conflict", body.get("error").asText());
        assertEquals("Email already registered", body.get("message").asText());
        assertEquals(USERS_PATH, body.get("path").asText());
        assertNotNull(walletCreateRequestBody.get());
        assertEquals(1, walletCreateRequestCount.get());
    }

    @Test
    void shouldReturnUserById() throws Exception {
        var payload = """
                {
                  "name": "Maria",
                  "cpf": "12345678901",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "COMMON"
                }
                """;

        var createResponse = sendRequest(USERS_PATH, "POST", payload);
        var createdId = readBody(createResponse).get("id").asLong();

        var response = sendRequest("%s/%s".formatted(USERS_PATH, createdId), "GET", null);
        var body = readBody(response);

        assertEquals(200, response.statusCode());
        assertEquals(createdId, body.get("id").asLong());
        assertEquals("Maria", body.get("name").asText());
        assertEquals("COMMON", body.get("type").asText());
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        var response = sendRequest("%s/99".formatted(USERS_PATH), "GET", null);
        var body = readBody(response);

        assertEquals(404, response.statusCode());
        assertEquals(404, body.get("status").asInt());
        assertEquals("Not Found", body.get("error").asText());
        assertEquals("User with id 99 not found", body.get("message").asText());
        assertEquals("%s/99".formatted(USERS_PATH), body.get("path").asText());
    }

    @Test
    void shouldRollbackUserCreationWhenWalletCreationFails() throws Exception {
        walletServiceStatus.set(500);
        walletServiceResponse.set(
                """
                {
                  "status": 500,
                  "error": "Internal Server Error",
                  "message": "Unexpected error",
                  "path": "/wallet"
                }
                """
        );

        var payload = """
                {
                  "name": "Maria",
                  "cpf": "12345678901",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "COMMON"
                }
                """;

        var response = sendRequest(USERS_PATH, "POST", payload);
        var body = readBody(response);
        var walletRequest = objectMapper.readTree(walletCreateRequestBody.get());

        assertEquals(502, response.statusCode());
        assertEquals(502, body.get("status").asInt());
        assertEquals("Bad Gateway", body.get("error").asText());
        assertEquals(
                "Failed to create wallet for user with id %s".formatted(walletRequest.get("userId").asLong()),
                body.get("message").asText()
        );
        assertFalse(userRepository.existsByEmail("maria@example.com"));
    }

    private HttpResponse<String> sendRequest(String path, String method, String payload) throws Exception {
        var builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:%s%s".formatted(port, path)));

        if (payload != null) {
            builder.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        }

        var request = switch (method) {
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(payload)).build();
            case "GET" -> builder.GET().build();
            default -> throw new IllegalArgumentException("Unsupported method: " + method);
        };

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private JsonNode readBody(HttpResponse<String> response) throws Exception {
        return objectMapper.readTree(response.body());
    }

    private static HttpServer createWalletServiceServer() {
        try {
            var server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/wallet", UserControllerIntegrationTest::handleWalletCreate);
            server.start();
            return server;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to start wallet service stub", exception);
        }
    }

    private static void handleWalletCreate(HttpExchange exchange) throws IOException {
        walletCreateRequestCount.incrementAndGet();
        walletCreateRequestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));

        var responseBody = walletServiceResponse.get();
        var responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        exchange.sendResponseHeaders(walletServiceStatus.get(), responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }
}
