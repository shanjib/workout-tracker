package com.workouttracker.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class AwsDatabaseConnectionProvider implements DatabaseConnectionProvider {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final DSLContext dslContext;

    public AwsDatabaseConnectionProvider(String secretArn, Region region) {
        this.dslContext = createContext(secretArn, region);
    }

    public DSLContext getContext() {
        return dslContext;
    }

    private static DSLContext createContext(String secretArn, Region region) {
        try {

            // Running in AWS — fetch credentials from Secrets Manager
            SecretsManagerClient client = SecretsManagerClient.builder()
                    .region(region)
                    .build();

            String secretJson = client.getSecretValue(
                    GetSecretValueRequest.builder()
                            .secretId(secretArn)
                            .build()
            ).secretString();

            Map<String, String> secret = objectMapper.readValue(secretJson, Map.class);
            String username = secret.get("username");
            String password = secret.get("password");
            String host = secret.get("host");
            String port = secret.get("port");
            String dbName = secret.get("dbname");
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);

            // Run Flyway migrations
            Flyway.configure()
                    .dataSource(jdbcUrl, username, password)
                    .locations("classpath:db/migration")
                    .load()
                    .migrate();

            // Create and return jOOQ context
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            return DSL.using(connection, SQLDialect.POSTGRES);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }
}
