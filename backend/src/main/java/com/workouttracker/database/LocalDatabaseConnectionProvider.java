package com.workouttracker.database;

import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

public class LocalDatabaseConnectionProvider implements DatabaseConnectionProvider {

    private final DSLContext dslContext;

    public LocalDatabaseConnectionProvider() {
        this.dslContext = createContext();
    }

    public DSLContext getContext() {
        return dslContext;
    }

    private static DSLContext createContext() {
        try {
            // Running locally — use local Docker PostgreSQL
            String jdbcUrl = "jdbc:postgresql://localhost:5432/workoutdb";
            String username = "postgres";
            String password = "password";

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
