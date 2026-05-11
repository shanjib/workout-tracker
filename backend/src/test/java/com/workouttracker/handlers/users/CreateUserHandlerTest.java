package com.workouttracker.handlers.users;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.workouttracker.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserHandlerTest extends BaseIntegrationTest {


    @Test
    void createsUserAndReturns201() {
        CreateUserHandler handler = new CreateUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withBody("{\"dateOfBirth\": \"1990-01-01\", \"unitSystem\": \"METRIC\"}")
                .withRequestContext(mockRequestContext("test-cognito-sub"));

        var response = handler.handleRequest(request, null);

        assertEquals(201, response.getStatusCode());
        assertTrue(response.getBody().contains("test-cognito-sub"));
    }

    @Test
    void createsUserWithNullableFieldsOmitted() {
        CreateUserHandler handler = new CreateUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withBody("{}")
                .withRequestContext(mockRequestContext("test-cognito-sub-2"));

        var response = handler.handleRequest(request, null);

        assertEquals(201, response.getStatusCode());
    }

    @Test
    void returns500ForMalformedBody() {
        CreateUserHandler handler = new CreateUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withBody("not-valid-json")
                .withRequestContext(mockRequestContext("test-cognito-sub-3"));

        var response = handler.handleRequest(request, null);

        assertEquals(500, response.getStatusCode());
    }

    private APIGatewayProxyRequestEvent.ProxyRequestContext mockRequestContext(String cognitoSub) {
        var context = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        context.setAuthorizer(Map.of("claims", Map.of("sub", cognitoSub)));
        return context;
    }
}
