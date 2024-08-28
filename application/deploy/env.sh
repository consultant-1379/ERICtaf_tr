#!/bin/sh
SSHPASS=/proj/PDU_OSS_CI_TAF/tools/sshpass-1.05/sshpass

case "$ENV" in
prod )
  HOST=eselivm2v637l.lmera.ericsson.se
  USER=tafuser
  PASSWORD="$PASSWORD_PROD"
  WEBAPP_DIR=/proj/PDU_OSS_CI_TAF/taf-registry/webapps
  ;;
*)
  echo "Invalid environment: '$ENV'"
  exit 1
esac

echo "
Deployment environment:
HOST=${HOST}
USER=${USER}
WEBAPP_DIR=${WEBAPP_DIR}
"
