CREATE TABLE users (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cognito_sub      VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth    DATE,
    unit_system      VARCHAR(10) CHECK (unit_system IN ('METRIC', 'IMPERIAL')),
    created_at       TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP NOT NULL DEFAULT NOW()
);
