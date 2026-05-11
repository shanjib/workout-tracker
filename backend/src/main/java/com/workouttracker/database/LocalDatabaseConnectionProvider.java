package com.workouttracker.database;

import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

public class LocalDatabaseConnectionProvider implements DatabaseConnectionProvider {

    private final String url;
    private final String user;
    private final String password;

    public LocalDatabaseConnectionProvider() {
        this.url = System.getProperty("TEST_DB_URL", "jdbc:postgresql://localhost:5432/workoutdb");
        this.user = System.getProperty("TEST_DB_USER", "postgres");
        this.password = System.getProperty("TEST_DB_PASSWORD", "password");
        runMigrations();
    }

    private void runMigrations() {
        Flyway.configure()
                .dataSource(url, user, password)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }

    @Override
    public DSLContext getContext() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            return DSL.using(connection, SQLDialect.POSTGRES);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create database connection", e);
        }
    }
}
