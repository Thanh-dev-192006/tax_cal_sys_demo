FROM registry.access.redhat.com/ubi9/openjdk-21 AS build

USER 0

WORKDIR /app

COPY lib/ lib/
COPY src/ src/

RUN mkdir -p bin \
    && find src -name "*.java" | sort > sources.list \
    && javac -encoding UTF-8 --release 17 -cp "lib/*" -d bin @sources.list

FROM eclipse-temurin:21

RUN apt-get update && apt-get install -y --no-install-recommends \
        netcat-openbsd \
        novnc \
        python3-websockify \
        x11vnc \
        xfonts-base \
        xvfb \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY lib/ lib/
COPY --from=build /app/bin/ bin/
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh

RUN chmod +x /usr/local/bin/docker-entrypoint.sh \
    && ln -s /usr/share/novnc/vnc.html /usr/share/novnc/index.html

ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8" \
    DB_HOST=db \
    DB_PORT=3306 \
    DB_NAME=tax_return_system \
    DB_USER=tax_app \
    DB_PASSWORD=tax_app_password \
    APP_PORT=8080 \
    DISPLAY=:99 \
    VNC_PORT=5900 \
    NOVNC_PORT=6080 \
    SCREEN_WIDTH=1440 \
    SCREEN_HEIGHT=900 \
    SCREEN_DEPTH=24 \
    COMPILE_ON_START=false

EXPOSE 5900 6080

CMD ["/usr/local/bin/docker-entrypoint.sh"]
