#!/bin/sh
set -eu

psql -v ON_ERROR_STOP=1 \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=migration_password="$MIGRATION_DB_PASSWORD" \
  --set=app_password="$APP_DB_PASSWORD" <<'EOSQL'
CREATE ROLE antiguedades_migrator
  LOGIN
  PASSWORD :'migration_password'
  NOSUPERUSER
  NOCREATEDB
  NOCREATEROLE
  NOINHERIT;

CREATE ROLE antiguedades_app
  LOGIN
  PASSWORD :'app_password'
  NOSUPERUSER
  NOCREATEDB
  NOCREATEROLE
  NOINHERIT;

ALTER DATABASE antiguedades OWNER TO antiguedades_migrator;
ALTER SCHEMA public OWNER TO antiguedades_migrator;

GRANT CONNECT ON DATABASE antiguedades TO antiguedades_app;
GRANT USAGE ON SCHEMA public TO antiguedades_app;

ALTER DEFAULT PRIVILEGES FOR ROLE antiguedades_migrator IN SCHEMA public
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO antiguedades_app;
ALTER DEFAULT PRIVILEGES FOR ROLE antiguedades_migrator IN SCHEMA public
  GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO antiguedades_app;
EOSQL
