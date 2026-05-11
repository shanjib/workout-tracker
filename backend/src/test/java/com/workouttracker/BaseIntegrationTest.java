package com.workouttracker;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("workoutdb")
            .withUsername("postgres")
            .withPassword("password");

    @BeforeEach
    void resetDatabase() {
        System.setProperty("TEST_DB_URL", postgres.getJdbcUrl());
        System.setProperty("TEST_DB_USER", postgres.getUsername());
        System.setProperty("TEST_DB_PASSWORD", postgres.getPassword());

        Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .cleanDisabled(false)
                .load()
                .clean();

        Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }
}
