#!/bin/sh
set -eu

psql --set=ON_ERROR_STOP=1 \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=app_password="$APP_DB_PASSWORD" <<'SQL'
SELECT format('CREATE ROLE antiguedades_app LOGIN PASSWORD %L', :'app_password')
WHERE NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'antiguedades_app') \gexec

SELECT format('ALTER ROLE antiguedades_app PASSWORD %L', :'app_password') \gexec
GRANT CONNECT ON DATABASE antiguedades TO antiguedades_app;
SQL
