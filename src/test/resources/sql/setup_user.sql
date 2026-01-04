-- Clear existing test data to prevent primary key conflicts
DELETE FROM users WHERE email = 'testuser@example.com';

-- Insert the known state for the test
INSERT INTO users (username, email, password, role, created_at)
VALUES (
    'TestUser123',
    'testuser@example.com',
    '$2a$10$8K1p/a8...', -- Pre-hashed password
    'USER',
    CURRENT_TIMESTAMP
);