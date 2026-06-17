INSERT INTO app_users (email, password, role, name)
VALUES (
    'superusuario@gmail.com',
    '$2a$10$XL/qr3h9Gv212Y6o6IbrWe.mD5AdS0U2egVT61KG24eM3YBOyLIAu',
    'admin',
    'Superusuario'
)
ON CONFLICT (email) DO UPDATE SET
    password = EXCLUDED.password,
    role = EXCLUDED.role,
    name = EXCLUDED.name;
