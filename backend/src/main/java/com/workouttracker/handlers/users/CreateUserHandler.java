package com.workouttracker.handlers.users;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workouttracker.database.DatabaseConnectionFactory;
import com.workouttracker.database.DatabaseConnectionProvider;
import com.workouttracker.generated.tables.Users;

import java.time.LocalDate;
import java.util.Map;

public class CreateUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final DatabaseConnectionProvider db = DatabaseConnectionFactory.create();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> claims = (Map<String, String>) input.getRequestContext()
                    .getAuthorizer()
                    .get("claims");
            String cognitoSub = claims.get("sub");

            Map<String, Object> body = objectMapper.readValue(input.getBody(), Map.class);

            var record = db.getContext().newRecord(Users.USERS);
            record.setCognitoSub(cognitoSub);

            if (body.get("dateOfBirth") != null) {
                record.setDateOfBirth(LocalDate.parse((String) body.get("dateOfBirth")));
            }
            if (body.get("unitSystem") != null) {
                record.setUnitSystem((String) body.get("unitSystem"));
            }

            record.store();

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(201)
                    .withBody(objectMapper.writeValueAsString(record.intoMap()));

        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\": \"Internal server error\"}");
        }
    }
}
