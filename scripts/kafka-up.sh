cat > scripts/kafka-up.sh <<'EOF'
#!/usr/bin/env bash
set -euo pipefail

COMPOSE_FILE="${COMPOSE_FILE:-kafka/compose.yml}"

echo "[kafka-up] Using compose file: ${COMPOSE_FILE}"
docker compose -f "${COMPOSE_FILE}" up -d

echo "[kafka-up] Containers:"
docker ps --filter "name=^/kafka$" --filter "name=^/kafka-ui$"
EOF

chmod +x scripts/kafka-up.sh
