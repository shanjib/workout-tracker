package com.workouttracker.database;

import org.jooq.DSLContext;

public interface DatabaseConnectionProvider {
    DSLContext getContext();
}
