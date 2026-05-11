package com.workouttracker.handlers.users;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.workouttracker.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GetUserHandlerTest extends BaseIntegrationTest {

    private String createdUserId;

    @BeforeEach
    void createUser() throws Exception {
        CreateUserHandler createHandler = new CreateUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withBody("{\"dateOfBirth\": \"1990-01-01\", \"unitSystem\": \"METRIC\"}")
                .withRequestContext(mockRequestContext("get-test-sub"));

        var response = createHandler.handleRequest(request, null);
        var body = new com.fasterxml.jackson.databind.ObjectMapper()
                .readValue(response.getBody(), Map.class);
        createdUserId = body.get("id").toString();
    }

    @Test
    void returnsUserFor200() {
        GetUserHandler handler = new GetUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withPathParameters(Map.of("id", createdUserId));

        var response = handler.handleRequest(request, null);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("get-test-sub"));
    }

    @Test
    void returns404ForNonExistentUser() {
        GetUserHandler handler = new GetUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withPathParameters(Map.of("id", "00000000-0000-0000-0000-000000000000"));

        var response = handler.handleRequest(request, null);

        assertEquals(404, response.getStatusCode());
    }

    private APIGatewayProxyRequestEvent.ProxyRequestContext mockRequestContext(String cognitoSub) {
        var context = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        context.setAuthorizer(Map.of("claims", Map.of("sub", cognitoSub)));
        return context;
    }
}
