FROM adoptopenjdk:latest as builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract


FROM adoptopenjdk:latest
RUN addgroup --system app && adduser --system --ingroup app app

USER app
VOLUME /tmp
WORKDIR /app

COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

COPY opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.metrics.exporter=none -Dotel.exporter.otlp.endpoint=http://otel-collector.opensearch.svc.cluster.local:4317 -Dotel.resource.attributes="service.name=test-app" org.springframework.boot.loader.JarLauncher
