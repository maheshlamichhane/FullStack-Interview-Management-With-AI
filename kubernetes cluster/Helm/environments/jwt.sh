#!/bin/bash
# create-jwks-secret.sh

KEYCLOAK_URL="http://localhost:8084"
REALM="Interview"

echo "Fetching JWKS from Keycloak..."
JWKS_JSON=$(curl -s "${KEYCLOAK_URL}/realms/${REALM}/protocol/openid-connect/certs")

if [ -z "$JWKS_JSON" ]; then
  echo "ERROR: Failed to fetch JWKS"
  exit 1
fi

echo "Creating secret YAML file..."
cat > keycloak-jwks-secret.yaml <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: keycloak-jwks
  namespace: dev
  labels:
    app: interview-system
    component: jwt-auth
type: nginx.com/jwk
data:
  jwk: $(echo "$JWKS_JSON" | base64 | tr -d '\n')
EOF

echo "Applying secret..."
kubectl apply -f keycloak-jwks-secret.yaml

echo "✅ Secret created successfully!"