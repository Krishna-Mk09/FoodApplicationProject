#!/bin/bash
echo "⏳ Waiting for Kafka Connect to start..."

# Wait until Kafka Connect REST API is up
until curl -s http://kafka-connect:8083/ >/dev/null 2>&1; do
    echo "Waiting for Kafka Connect..."
    sleep 5
done

echo "📡 Kafka Connect is up, registering MySQL Debezium connector..."

# Register the connector
curl -X POST -H "Content-Type: application/json" \
     --data @/scripts/register-mysql-connector.json \
     http://kafka-connect:8083/connectors

echo "✅ MySQL Debezium Connector registered successfully!"

# Keep container alive
tail -f /dev/null
