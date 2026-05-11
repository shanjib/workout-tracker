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
import java.util.UUID;

public class UpdateUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUserHandler.class);
    private final DatabaseConnectionProvider db;

    public UpdateUserHandler() {
        this.db = DatabaseConnectionFactory.create();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String id = input.getPathParameters().get("id");
            Map<String, Object> body = MapperUtil.getObjectMapper().readValue(input.getBody(), Map.class);

            var record = db.getContext()
                    .selectFrom(Users.USERS)
                    .where(Users.USERS.ID.eq(UUID.fromString(id)))
                    .fetchOne();

            if (record == null) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(404)
                        .withBody("{\"error\": \"User not found\"}");
            }

            if (body.get("dateOfBirth") != null) {
                record.setDateOfBirth(LocalDate.parse((String) body.get("dateOfBirth")));
            }
            if (body.get("unitSystem") != null) {
                record.setUnitSystem((String) body.get("unitSystem"));
            }

            record.store();

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(MapperUtil.getObjectMapper().writeValueAsString(record.intoMap()));

        } catch (Exception e) {
            logger.warn("Exception encountered: {}", e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\": \"Internal server error\"}");
        }
    }
}
