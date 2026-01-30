-- في user-service/src/main/resources/data.sql
INSERT INTO users (id, nom, prenom, email, password, phone, date_naissance, role, active, user_type)
VALUES (
           '550e8400-e29b-41d4-a716-446655440000',
           'Admin',
           'System',
           'admin@school.com',
           '$2a$12$r8pBqyQfK6mW3X7V2nT5cOeN9PwRtSxYzVbCcDfGhJkLmNqPwRtSx', -- hash لـ admin123
           '0600000000',
           '1990-01-01',
           'ADMIN',
           true,
           'ADMIN'
       ) ON CONFLICT (email) DO NOTHING;

INSERT INTO admin (id, matricule)
VALUES (
           '550e8400-e29b-41d4-a716-446655440000',
           'ADM001'
       ) ON CONFLICT (id) DO NOTHING;