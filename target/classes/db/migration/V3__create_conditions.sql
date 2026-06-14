CREATE TABLE conditions (
    id BIGSERIAL PRIMARY KEY,
    label VARCHAR(100) NOT NULL
);

INSERT INTO conditions (label) VALUES
    ('Muy malo'),
    ('Malo'),
    ('Regular'),
    ('Bueno'),
    ('Muy bueno'),
    ('Excelente');
