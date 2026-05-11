package com.workouttracker.handlers.users;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.workouttracker.database.DatabaseConnectionFactory;
import com.workouttracker.database.DatabaseConnectionProvider;
import com.workouttracker.generated.tables.Users;
import com.workouttracker.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;

public class CreateUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);
    private final DatabaseConnectionProvider db;

    public CreateUserHandler() {
        this.db = DatabaseConnectionFactory.create();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> claims = (Map<String, String>) input.getRequestContext()
                    .getAuthorizer()
                    .get("claims");
            String cognitoSub = claims.get("sub");

            Map<String, Object> body = MapperUtil.getObjectMapper().readValue(input.getBody(), Map.class);

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
                    .withBody(MapperUtil.getObjectMapper().writeValueAsString(record.intoMap()));

        } catch (Exception e) {
            logger.warn("Exception encountered: {}", e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
