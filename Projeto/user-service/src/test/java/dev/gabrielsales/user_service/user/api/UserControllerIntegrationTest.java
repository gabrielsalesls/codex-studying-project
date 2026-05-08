package dev.gabrielsales.user_service.user.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gabrielsales.user_service.user.UserRepository;
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
class UserControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateUser() throws Exception {
        userRepository.deleteAll();

        var payload = """
                {
                  "name": "Maria",
                  "cpf": "12345678901",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "COMMON"
                }
                """;

        var response = sendRequest("/users", "POST", payload);
        var body = readBody(response);

        var createdId = body.get("id").asLong();

        assertEquals(201, response.statusCode());
        assertEquals("/users/%s".formatted(createdId), response.headers().firstValue("Location").orElseThrow());
        assertEquals(createdId, body.get("id").asLong());
        assertEquals("Maria", body.get("name").asText());
        assertEquals("COMMON", body.get("type").asText());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        userRepository.deleteAll();

        var payload = """
                {
                  "name": "Maria",
                  "cpf": "12345678901",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "COMMON"
                }
                """;

        sendRequest("/users", "POST", payload);

        var duplicateEmailPayload = """
                {
                  "name": "Joao",
                  "cpf": "98765432100",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "MERCHANT"
                }
                """;

        var response = sendRequest("/users", "POST", duplicateEmailPayload);
        var body = readBody(response);

        assertEquals(409, response.statusCode());
        assertEquals(409, body.get("status").asInt());
        assertEquals("Conflict", body.get("error").asText());
        assertEquals("Email already registered", body.get("message").asText());
        assertEquals("/users", body.get("path").asText());
    }

    @Test
    void shouldReturnUserById() throws Exception {
        userRepository.deleteAll();

        var payload = """
                {
                  "name": "Maria",
                  "cpf": "12345678901",
                  "email": "maria@example.com",
                  "password": "secret",
                  "type": "COMMON"
                }
                """;

        var createResponse = sendRequest("/users", "POST", payload);
        var createdId = readBody(createResponse).get("id").asLong();

        var response = sendRequest("/users/%s".formatted(createdId), "GET", null);
        var body = readBody(response);

        assertEquals(200, response.statusCode());
        assertEquals(createdId, body.get("id").asLong());
        assertEquals("Maria", body.get("name").asText());
        assertEquals("COMMON", body.get("type").asText());
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        userRepository.deleteAll();

        var response = sendRequest("/users/99", "GET", null);
        var body = readBody(response);

        assertEquals(404, response.statusCode());
        assertEquals(404, body.get("status").asInt());
        assertEquals("Not Found", body.get("error").asText());
        assertEquals("User with id 99 not found", body.get("message").asText());
        assertEquals("/users/99", body.get("path").asText());
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
}
