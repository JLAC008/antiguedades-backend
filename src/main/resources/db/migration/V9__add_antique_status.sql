ALTER TABLE antiques
    ADD COLUMN status VARCHAR(20);

ALTER TABLE antiques
    ADD CONSTRAINT chk_antiques_status
    CHECK (status IS NULL OR status IN ('reservado', 'pagado', 'vendido', 'enviado'));
