#!/bin/bash

echo "⏳ Waiting for Kafka Connect..."

# Wait for Kafka Connect REST API
until curl -s http://kafka-connect:8083/ >/dev/null; do
  echo "Waiting for Kafka Connect..."
  sleep 5
done

echo "⏳ Waiting for Kafka to be ready..."

# OPTIONAL: Instead of nc, just wait a bit (Kafka is already dependency)
sleep 10

echo "📡 Checking if connector already exists..."

# Check if connector exists
if curl -s http://kafka-connect:8083/connectors/mysql-connector | grep -q "mysql-connector"; then
  echo "⚠️ Connector already exists. Deleting..."
  curl -X DELETE http://kafka-connect:8083/connectors/mysql-connector
  sleep 5
fi

echo "🚀 Registering MySQL Debezium connector..."

response=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
  -H "Content-Type: application/json" \
  --data @/scripts/register-mysql-connector.json \
  http://kafka-connect:8083/connectors)

if [ "$response" = "201" ]; then
  echo "✅ Connector registered successfully!"
else
  echo "❌ Failed to register connector. HTTP status: $response"
  exit 1
fi

echo "🎉 Done!"