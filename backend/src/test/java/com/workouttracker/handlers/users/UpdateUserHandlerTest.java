package com.workouttracker.handlers.users;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.workouttracker.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserHandlerTest extends BaseIntegrationTest {

    private String createdUserId;

    @BeforeEach
    void createUser() throws Exception {
        CreateUserHandler createHandler = new CreateUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withBody("{\"dateOfBirth\": \"1990-01-01\", \"unitSystem\": \"METRIC\"}")
                .withRequestContext(mockRequestContext("update-test-sub"));

        var response = createHandler.handleRequest(request, null);
        var body = new com.fasterxml.jackson.databind.ObjectMapper()
                .readValue(response.getBody(), Map.class);
        createdUserId = body.get("id").toString();
    }

    @Test
    void updatesUserAndReturns200() {
        UpdateUserHandler handler = new UpdateUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withPathParameters(Map.of("id", createdUserId))
                .withBody("{\"unitSystem\": \"IMPERIAL\"}");

        var response = handler.handleRequest(request, null);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("IMPERIAL"));
    }

    @Test
    void returns404ForNonExistentUser() {
        UpdateUserHandler handler = new UpdateUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withPathParameters(Map.of("id", "00000000-0000-0000-0000-000000000000"))
                .withBody("{\"unitSystem\": \"IMPERIAL\"}");

        var response = handler.handleRequest(request, null);

        assertEquals(404, response.getStatusCode());
    }

    private APIGatewayProxyRequestEvent.ProxyRequestContext mockRequestContext(String cognitoSub) {
        var context = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        context.setAuthorizer(Map.of("claims", Map.of("sub", cognitoSub)));
        return context;
    }
}
