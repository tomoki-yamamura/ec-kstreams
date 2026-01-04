cat > scripts/kafka-wait.sh <<'EOF'
#!/usr/bin/env bash
set -euo pipefail

KAFKA_CONTAINER="${KAFKA_CONTAINER:-kafka}"
BOOTSTRAP="${BOOTSTRAP:-localhost:9092}"
MAX_RETRY="${MAX_RETRY:-60}"
SLEEP_SEC="${SLEEP_SEC:-1}"

echo "[kafka-wait] Waiting Kafka in container '${KAFKA_CONTAINER}' (${BOOTSTRAP})..."

for i in $(seq 1 "${MAX_RETRY}"); do
  if docker exec "${KAFKA_CONTAINER}" bash -lc "kafka-broker-api-versions.sh --bootstrap-server ${BOOTSTRAP} >/dev/null 2>&1"; then
    echo "[kafka-wait] Kafka is ready."
    exit 0
  fi
  echo "[kafka-wait] not ready yet... (${i}/${MAX_RETRY})"
  sleep "${SLEEP_SEC}"
done

echo "[kafka-wait] ERROR: Kafka did not become ready in time." >&2
exit 1
EOF

chmod +x scripts/kafka-wait.sh
