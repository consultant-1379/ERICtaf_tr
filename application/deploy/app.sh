#!/bin/sh
source `dirname $0`/env.sh
SERVER_ARTIFACT=tr-backend

echo "Uploading artifacts"
${SSHPASS} -p "$PASSWORD" scp application/backend/target/${SERVER_ARTIFACT}-*.war ${USER}@${HOST}:/tmp/${SERVER_ARTIFACT}.war

echo "Running deployment commands"
${SSHPASS} -p "$PASSWORD" ssh ${USER}@${HOST} <<EOF
echo "Moving web app"
rm -rf ${WEBAPP_DIR}/ROOT.war
rm -rf ${WEBAPP_DIR}/ROOT

mv -f /tmp/${SERVER_ARTIFACT}.war ${WEBAPP_DIR}/ROOT.war
EOF
