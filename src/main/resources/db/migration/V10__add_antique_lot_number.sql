CREATE SEQUENCE antique_lot_number_seq
    AS BIGINT
    START WITH 1
    INCREMENT BY 1;

ALTER TABLE antiques
    ADD COLUMN lot_number BIGINT;

WITH numbered AS (
    SELECT id,
           ROW_NUMBER() OVER (ORDER BY created_at ASC, id ASC) AS lot_number
    FROM antiques
)
UPDATE antiques a
SET lot_number = numbered.lot_number
FROM numbered
WHERE a.id = numbered.id;

SELECT setval(
    'antique_lot_number_seq',
    COALESCE(MAX(lot_number), 1),
    COUNT(*) > 0
)
FROM antiques;

ALTER TABLE antiques
    ALTER COLUMN lot_number SET DEFAULT nextval('antique_lot_number_seq');

ALTER TABLE antiques
    ALTER COLUMN lot_number SET NOT NULL;

ALTER TABLE antiques
    ADD CONSTRAINT uk_antiques_lot_number UNIQUE (lot_number);

GRANT USAGE, SELECT ON SEQUENCE antique_lot_number_seq TO antiguedades_app;
