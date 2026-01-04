-- Remove only the specific data created by the test
DELETE FROM users WHERE email = 'testuser@example.com';

-- Optional: Reset auto-increment counters if your DB supports it (e.g., H2 or MySQL)
-- ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
```

### How to use these in your Java Tests
In your test class, you can now use your `DbUtils` to execute these files:

```java
@BeforeEach
void setup() {
    // Path relative to resources
    DbUtils.executeSqlScript("sql/setup_user.sql");
}

@AfterEach
void tearDown() {
    DbUtils.executeSqlScript("sql/teardown_user.sql");
}
```

### Pro-Tips for SQL Testing
1. **Hardcoded IDs vs. Emails:** It is usually safer to delete/setup based on a unique `email` or `username` rather than a hardcoded `id`, as some databases handle auto-incrementing IDs differently.
2. **Foreign Keys:** If your `users` table has related tables (like `posts` or `profiles`), your `teardown_user.sql` must delete the child records first to avoid Foreign Key Constraint violations.
3. **Naming Convention:** I recommend naming them after the feature, e.g., `setup_auth.sql`, `setup_admin_privileges.sql`, etc.