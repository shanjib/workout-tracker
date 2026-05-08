package com.workouttracker.database;

import software.amazon.awssdk.regions.Region;

public class DatabaseConnectionFactory {

    public static DatabaseConnectionProvider create() {
        String secretArn = System.getenv("DB_SECRET_ARN");
        String regionString = System.getenv("AWS_REGION");

        if (secretArn == null || secretArn.isEmpty()) {
            return new LocalDatabaseConnectionProvider();
        }

        Region region = Region.regions().stream()
                .filter(r -> r.id().equals(regionString))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unsupported region: " + regionString));

        return new AwsDatabaseConnectionProvider(secretArn, region);
    }
}
