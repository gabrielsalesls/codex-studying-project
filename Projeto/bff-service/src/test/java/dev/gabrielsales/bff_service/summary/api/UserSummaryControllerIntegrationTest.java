package dev.gabrielsales.bff_service.summary.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserSummaryControllerIntegrationTest {

    private static final String USERS_PATH = "/users";
    private static final HttpServer userServiceServer = createServer(UserSummaryControllerIntegrationTest::handleUserRequest);
    private static final HttpServer walletServiceServer = createServer(UserSummaryControllerIntegrationTest::handleWalletRequest);

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("user-service.base-url", () -> "http://localhost:%s".formatted(userServiceServer.getAddress().getPort()));
        registry.add("wallet-service.base-url", () -> "http://localhost:%s".formatted(walletServiceServer.getAddress().getPort()));
    }

    @AfterAll
    static void stopServers() {
        userServiceServer.stop(0);
        walletServiceServer.stop(0);
    }

    @Test
    void shouldReturnAggregatedUserSummary() throws Exception {
        var response = sendGet("/users/1/summary");
        var body = readBody(response);

        assertEquals(200, response.statusCode());
        assertEquals(1L, body.get("userId").asLong());
        assertEquals("Maria", body.get("name").asText());
        assertEquals("COMMON", body.get("type").asText());
        assertEquals(0, body.get("balance").decimalValue().compareTo(new java.math.BigDecimal("100.50")));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        var response = sendGet("/users/99/summary");
        var body = readBody(response);

        assertEquals(404, response.statusCode());
        assertEquals("User with id 99 not found", body.get("message").asText());
    }

    @Test
    void shouldReturnNotFoundWhenWalletDoesNotExist() throws Exception {
        var response = sendGet("/users/2/summary");
        var body = readBody(response);

        assertEquals(404, response.statusCode());
        assertEquals("Wallet not found for user with id 2", body.get("message").asText());
    }

    private HttpResponse<String> sendGet(String path) throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:%s%s".formatted(port, path)))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private JsonNode readBody(HttpResponse<String> response) throws Exception {
        return objectMapper.readTree(response.body());
    }

    private static HttpServer createServer(HttpHandler handler) {
        try {
            var server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/", exchange -> handler.handle(exchange));
            server.start();
            return server;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to start HTTP stub", exception);
        }
    }

    private static void handleUserRequest(HttpExchange exchange) throws IOException {
        var path = exchange.getRequestURI().getPath();

        if ("/api/v1/users/1".equals(path)) {
            writeResponse(exchange, 200, """
                    {
                      "id": 1,
                      "name": "Maria",
                      "type": "COMMON"
                    }
                    """);
            return;
        }

        if ("/api/v1/users/2".equals(path)) {
            writeResponse(exchange, 200, """
                    {
                      "id": 2,
                      "name": "Joao",
                      "type": "MERCHANT"
                    }
                    """);
            return;
        }

        writeResponse(exchange, 404, """
                {
                  "status": 404,
                  "error": "Not Found",
                  "message": "User with id 99 not found",
                  "path": "%s"
                }
                """.formatted(path));
    }

    private static void handleWalletRequest(HttpExchange exchange) throws IOException {
        var path = exchange.getRequestURI().getPath();

        if ("/wallet/1".equals(path)) {
            writeResponse(exchange, 200, """
                    {
                      "userId": 1,
                      "balance": 100.50
                    }
                    """);
            return;
        }

        writeResponse(exchange, 404, """
                {
                  "status": 404,
                  "error": "Not Found",
                  "message": "Wallet not found for user with id 2",
                  "path": "%s"
                }
                """.formatted(path));
    }

    private static void writeResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        var responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    @FunctionalInterface
    private interface HttpHandler {
        void handle(HttpExchange exchange) throws IOException;
    }
}
