CREATE UNIQUE INDEX uk_antiques_normalized_name
    ON antiques (LOWER(BTRIM(name)));
