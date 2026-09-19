UPDATE antiques
SET status = NULL
WHERE status = 'vendido';

ALTER TABLE antiques
    DROP CONSTRAINT chk_antiques_status;

ALTER TABLE antiques
    ADD CONSTRAINT chk_antiques_status
    CHECK (status IS NULL OR status IN ('reservado', 'pagado', 'enviado'));
