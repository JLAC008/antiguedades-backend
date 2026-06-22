ALTER TABLE antiques
    ADD COLUMN allow_duplicate_name BOOLEAN NOT NULL DEFAULT FALSE;

DROP INDEX uk_antiques_normalized_name;

CREATE UNIQUE INDEX uk_antiques_non_repeatable_name
    ON antiques (LOWER(BTRIM(name)))
    WHERE allow_duplicate_name = FALSE;
