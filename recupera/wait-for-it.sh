#!/bin/sh

set -e

until nc -z -v -w30 postgres 5432
do
  echo "Aguardando conexão com o PostgreSQL..."
  sleep 5
done

echo "PostgreSQL is up - executing command"