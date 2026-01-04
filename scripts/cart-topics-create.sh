cat > scripts/cart-topics-create.sh <<'EOF'
#!/usr/bin/env bash
set -euo pipefail

KAFKA_CONTAINER="${KAFKA_CONTAINER:-kafka}"
BOOTSTRAP="${BOOTSTRAP:-localhost:9092}"

CART_EVENTS_TOPIC="${CART_EVENTS_TOPIC:-commerce.cart.events.v1}"
CART_SNAPSHOTS_TOPIC="${CART_SNAPSHOTS_TOPIC:-commerce.cart.snapshots.v1}"

CART_EVENTS_PARTITIONS="${CART_EVENTS_PARTITIONS:-3}"
CART_SNAPSHOTS_PARTITIONS="${CART_SNAPSHOTS_PARTITIONS:-3}"

CART_EVENTS_RETENTION_MS="${CART_EVENTS_RETENTION_MS:-604800000}"

ENABLE_SNAPSHOTS="${ENABLE_SNAPSHOTS:-true}"

echo "[cart-topics] Creating topics (container=${KAFKA_CONTAINER}, bootstrap=${BOOTSTRAP})"

create_topic_if_missing () {
  local topic="$1"
  local partitions="$2"

  if docker exec "${KAFKA_CONTAINER}" bash -lc "kafka-topics.sh --bootstrap-server ${BOOTSTRAP} --list | grep -Fx '${topic}' >/dev/null"; then
    echo "[cart-topics] exists: ${topic} (skip)"
  else
    echo "[cart-topics] create: ${topic}"
    docker exec "${KAFKA_CONTAINER}" bash -lc \
      "kafka-topics.sh --bootstrap-server ${BOOTSTRAP} --create --topic '${topic}' --partitions ${partitions} --replication-factor 1"
  fi
}

create_topic_if_missing "${CART_EVENTS_TOPIC}" "${CART_EVENTS_PARTITIONS}"

echo "[cart-topics] set retention.ms=${CART_EVENTS_RETENTION_MS} for ${CART_EVENTS_TOPIC}"
docker exec "${KAFKA_CONTAINER}" bash -lc \
  "kafka-configs.sh --bootstrap-server ${BOOTSTRAP} --entity-type topics --entity-name '${CART_EVENTS_TOPIC}' --alter --add-config retention.ms=${CART_EVENTS_RETENTION_MS}"

if [[ "${ENABLE_SNAPSHOTS}" == "true" ]]; then
  create_topic_if_missing "${CART_SNAPSHOTS_TOPIC}" "${CART_SNAPSHOTS_PARTITIONS}"

  echo "[cart-topics] set cleanup.policy=compact for ${CART_SNAPSHOTS_TOPIC}"
  docker exec "${KAFKA_CONTAINER}" bash -lc \
    "kafka-configs.sh --bootstrap-server ${BOOTSTRAP} --entity-type topics --entity-name '${CART_SNAPSHOTS_TOPIC}' --alter --add-config cleanup.policy=compact"
else
  echo "[cart-topics] snapshots disabled (ENABLE_SNAPSHOTS=false)"
fi

echo "[cart-topics] done."
EOF

chmod +x scripts/cart-topics-create.sh
