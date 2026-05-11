package com.workouttracker.handlers.users;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.workouttracker.BaseIntegrationTest;
import com.workouttracker.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeleteUserHandlerTest extends BaseIntegrationTest {

    private String createdUserId;

    @BeforeEach
    void createUser() throws Exception {
        CreateUserHandler createHandler = new CreateUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withBody("{\"dateOfBirth\": \"1990-01-01\", \"unitSystem\": \"METRIC\"}")
                .withRequestContext(mockRequestContext("delete-test-sub"));

        var response = createHandler.handleRequest(request, null);
        var body = MapperUtil.getObjectMapper()
                .readValue(response.getBody(), Map.class);
        createdUserId = body.get("id").toString();
    }

    @Test
    void deletesUserAndReturns204() {
        DeleteUserHandler handler = new DeleteUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withPathParameters(Map.of("id", createdUserId));

        var response = handler.handleRequest(request, null);

        assertEquals(204, response.getStatusCode());
    }

    @Test
    void returns404ForNonExistentUser() {
        DeleteUserHandler handler = new DeleteUserHandler();
        var request = new APIGatewayProxyRequestEvent()
                .withPathParameters(Map.of("id", "00000000-0000-0000-0000-000000000000"));

        var response = handler.handleRequest(request, null);

        assertEquals(404, response.getStatusCode());
    }

    @Test
    void deletedUserCannotBeRetrieved() {
        DeleteUserHandler handler = new DeleteUserHandler();
        var deleteRequest = new APIGatewayProxyRequestEvent()
                .withPathParameters(Map.of("id", createdUserId));
        handler.handleRequest(deleteRequest, null);

        var getHandler = new GetUserHandler();
        var getRequest = new APIGatewayProxyRequestEvent()
                .withPathParameters(Map.of("id", createdUserId));
        var response = getHandler.handleRequest(getRequest, null);

        assertEquals(404, response.getStatusCode());
    }

    private APIGatewayProxyRequestEvent.ProxyRequestContext mockRequestContext(String cognitoSub) {
        var context = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        context.setAuthorizer(Map.of("claims", Map.of("sub", cognitoSub)));
        return context;
    }
}
