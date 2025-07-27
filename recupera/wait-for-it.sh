#!/bin/sh

set -e

HOST="$1"
PORT="$2"
shift 2

echo "Esperando conexão com $HOST:$PORT..."

until nc -z -v -w30 "$HOST" "$PORT"
do
  echo "Aguardando conexão com o PostgreSQL em $HOST:$PORT..."
  sleep 5
done

echo "PostgreSQL está disponível - iniciando aplicação"

exec "$@"
