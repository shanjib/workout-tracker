package com.workouttracker.handlers.users;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.workouttracker.database.DatabaseConnectionFactory;
import com.workouttracker.database.DatabaseConnectionProvider;
import com.workouttracker.generated.tables.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DeleteUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteUserHandler.class);
    private final DatabaseConnectionProvider db;

    public DeleteUserHandler() {
        this.db = DatabaseConnectionFactory.create();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String id = input.getPathParameters().get("id");

            int deleted = db.getContext()
                    .deleteFrom(Users.USERS)
                    .where(Users.USERS.ID.eq(UUID.fromString(id)))
                    .execute();

            if (deleted == 0) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(404)
                        .withBody("{\"error\": \"User not found\"}");
            }

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(204)
                    .withBody(null);

        } catch (Exception e) {
            logger.warn("Exception encountered: {}", e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\": \"Internal server error\"}");
        }
    }
}
