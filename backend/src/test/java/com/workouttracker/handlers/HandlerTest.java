package com.workouttracker.handlers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class HandlerTest {

    @Test
    void handlerReturns200() {
        Handler handler = new Handler();
        var response = handler.handleRequest(new APIGatewayProxyRequestEvent(), null);
        assertEquals(200, response.getStatusCode());
    }
}
