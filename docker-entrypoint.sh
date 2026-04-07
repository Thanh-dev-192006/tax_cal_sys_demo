#!/bin/sh
set -eu

DB_HOST="${DB_HOST:-db}"
DB_PORT="${DB_PORT:-3306}"
DB_NAME="${DB_NAME:-tax_return_system}"
DB_USER="${DB_USER:-tax_app}"
DB_PASSWORD="${DB_PASSWORD:-tax_app_password}"
APP_PORT="${APP_PORT:-8080}"
DISPLAY="${DISPLAY:-:99}"
VNC_PORT="${VNC_PORT:-5900}"
NOVNC_PORT="${NOVNC_PORT:-6080}"
SCREEN_WIDTH="${SCREEN_WIDTH:-1440}"
SCREEN_HEIGHT="${SCREEN_HEIGHT:-900}"
SCREEN_DEPTH="${SCREEN_DEPTH:-24}"
COMPILE_ON_START="${COMPILE_ON_START:-false}"

cat > /app/config.properties <<EOF
db.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=${DB_USER}
db.password=${DB_PASSWORD}
app.port=${APP_PORT}
EOF

echo "Waiting for MySQL at ${DB_HOST}:${DB_PORT}..."
until nc -z "${DB_HOST}" "${DB_PORT}"; do
    sleep 2
done
echo "MySQL is ready!"

if [ "${COMPILE_ON_START}" = "true" ]; then
    echo "Compiling Java sources..."
    mkdir -p /app/bin
    find /app/src -name "*.java" | sort > /tmp/sources.list
    javac -encoding UTF-8 --release 17 -cp "/app/lib/*" -d /app/bin @/tmp/sources.list
    echo "Compilation complete!"
fi

echo ""
echo "=========================================="
echo "Starting display server infrastructure..."
echo "=========================================="
echo ""

echo "1. Starting Xvfb virtual display on ${DISPLAY}..."
Xvfb "${DISPLAY}" -screen 0 "${SCREEN_WIDTH}x${SCREEN_HEIGHT}x${SCREEN_DEPTH}" &
XVFB_PID=$!
sleep 2
echo "   ✓ Xvfb started (PID: $XVFB_PID)"

echo "2. Starting x11vnc on port ${VNC_PORT}..."
x11vnc -display "${DISPLAY}" -forever -shared -rfbport "${VNC_PORT}" -nopw -listen 0.0.0.0 >/tmp/x11vnc.log 2>&1 &
X11VNC_PID=$!
sleep 2
echo "   ✓ x11vnc started (PID: $X11VNC_PID)"

echo "3. Starting websockify on port ${NOVNC_PORT}..."
websockify --web=/usr/share/novnc/ "${NOVNC_PORT}" "localhost:${VNC_PORT}" >/tmp/novnc.log 2>&1 &
WEBSOCKIFY_PID=$!
sleep 3
echo "   ✓ websockify started (PID: $WEBSOCKIFY_PID)"

echo ""
echo "=========================================="
echo "Services ready!"
echo "=========================================="
echo "VNC Display:  ${DISPLAY}"
echo "VNC Port:     ${VNC_PORT}"
echo "NoVNC Port:   ${NOVNC_PORT}"
echo "Access at:    http://localhost:${NOVNC_PORT}"
echo "=========================================="
echo ""

echo "Starting Java application..."
exec java -cp "lib/*:bin" com.oop.project.Main