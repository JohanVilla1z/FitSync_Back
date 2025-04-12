#!/bin/sh
# wait-for-mysql.sh

set -e

host="${MYSQLHOST:-mysql}"
port="${MYSQLPORT:-3306}"
timeout=60
waited=0

echo "Esperando a que MySQL esté disponible en $host:$port..."

until nc -z -v -w5 "$host" "$port" > /dev/null 2>&1; do
  waited=$((waited+5))
  
  if [ "$waited" -ge "$timeout" ]; then
    echo "Error: Tiempo de espera agotado para conectar a MySQL."
    exit 1
  fi
  
  echo "MySQL aún no está disponible, esperando 5 segundos... ($waited/$timeout)"
  sleep 5
done

echo "¡MySQL está disponible! Iniciando la aplicación..."