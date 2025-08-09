-- Insert roles only if they don't exist (using role_name as unique check)
INSERT INTO auth_role (id, role_name)
SELECT 'b4a9b70c-25e5-4160-8ddc-eb024ccd1002'::uuid, 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM auth_role WHERE role_name = 'ROLE_USER');

INSERT INTO auth_role (id, role_name)
SELECT '1cf87838-6a4f-4bcc-9686-2bf95562ef15'::uuid, 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM auth_role WHERE role_name = 'ROLE_ADMIN');

-- Insert users only if they don't exist (using email as unique check)
INSERT INTO auth_user (id, firstname, lastname, dob, email, password, is_active, is_locked, created_at)
SELECT '72f21064-6b0c-4733-8255-62de22e42c39'::uuid, 'John', 'Doe', '1990-01-01'::date, 'john.doe@example.com',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', true, false, CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM auth_user WHERE email = 'john.doe@example.com');

INSERT INTO auth_user (id, firstname, lastname, dob, email, password, is_active, is_locked, created_at)
SELECT 'f4482348-c1b3-4398-95dd-0a8c25e19a3b'::uuid, 'Jane', 'Smith', '1992-02-15'::date, 'jane.smith@example.com',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', true, false, CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM auth_user WHERE email = 'jane.smith@example.com');

-- Assign roles to users only if the assignments don't exist
INSERT INTO user_roles (auth_user_id, auth_role_id)
SELECT '72f21064-6b0c-4733-8255-62de22e42c39'::uuid, 'b4a9b70c-25e5-4160-8ddc-eb024ccd1002'::uuid
WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE auth_user_id = '72f21064-6b0c-4733-8255-62de22e42c39'::uuid AND auth_role_id = 'b4a9b70c-25e5-4160-8ddc-eb024ccd1002'::uuid);

INSERT INTO user_roles (auth_user_id, auth_role_id)
SELECT 'f4482348-c1b3-4398-95dd-0a8c25e19a3b'::uuid, 'b4a9b70c-25e5-4160-8ddc-eb024ccd1002'::uuid
WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE auth_user_id = 'f4482348-c1b3-4398-95dd-0a8c25e19a3b'::uuid AND auth_role_id = 'b4a9b70c-25e5-4160-8ddc-eb024ccd1002'::uuid);

INSERT INTO user_roles (auth_user_id, auth_role_id)
SELECT 'f4482348-c1b3-4398-95dd-0a8c25e19a3b'::uuid, '1cf87838-6a4f-4bcc-9686-2bf95562ef15'::uuid
WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE auth_user_id = 'f4482348-c1b3-4398-95dd-0a8c25e19a3b'::uuid AND auth_role_id = '1cf87838-6a4f-4bcc-9686-2bf95562ef15'::uuid);

-- Insert tokens only if they don't exist (using token value as unique check)
INSERT INTO token (token, issued_at, expires_at, auth_user_id)
SELECT 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9.signature',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days', '72f21064-6b0c-4733-8255-62de22e42c39'::uuid
WHERE NOT EXISTS (SELECT 1 FROM token WHERE token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9.signature');

INSERT INTO token (token, issued_at, expires_at, auth_user_id)
SELECT 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW5lLnNtaXRoQGV4YW1wbGUuY29tIn0.signature',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days', 'f4482348-c1b3-4398-95dd-0a8c25e19a3b'::uuid
WHERE NOT EXISTS (SELECT 1 FROM token WHERE token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW5lLnNtaXRoQGV4YW1wbGUuY29tIn0.signature');