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

public class GetUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(GetUserHandler.class);
    private final DatabaseConnectionProvider db;

    public GetUserHandler() {
        this.db = DatabaseConnectionFactory.create();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String id = input.getPathParameters().get("id");

            var user = db.getContext()
                    .selectFrom(Users.USERS)
                    .where(Users.USERS.ID.eq(java.util.UUID.fromString(id)))
                    .fetchOne();

            if (user == null) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(404)
                        .withBody("{\"error\": \"User not found\"}");
            }

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(MapperUtil.getObjectMapper().writeValueAsString(user.intoMap()));

        } catch (Exception e) {
            logger.warn("Exception encountered: {}", e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\": \"Internal server error\"}");
        }
    }
}
