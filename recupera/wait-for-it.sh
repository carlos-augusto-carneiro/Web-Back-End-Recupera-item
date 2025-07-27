#!/bin/sh

set -e

HOST=$1
PORT=$2

until nc -z -v -w30 "$HOST" "$PORT"
do
  echo "Aguardando conexão com o PostgreSQL em $HOST:$PORT..."
  sleep 5
done

echo "PostgreSQL está disponível - iniciando aplicação"

shift 2
if [ "$1" = "--" ]; then
  shift 1
fi

exec "$@"
