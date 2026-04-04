ARG TZ=America/Lima

FROM eclipse-temurin:25-jdk-ubi10-minimal AS build

ARG TZ

ENV TZ=${TZ}

WORKDIR /app

# Maven and dependencies
COPY .mvn/ .mvn/
COPY --chmod=0755 mvnw ./

COPY pom.xml ./

RUN ./mvnw -B dependency:go-offline -T 1C

# Build
COPY src src

RUN ./mvnw -B package -DskipTests


FROM gcr.io/distroless/java25 AS runtime

WORKDIR /app

ARG TZ

ENV TZ=${TZ}

COPY --chown=185 --from=build /app/target/quarkus-app/lib/ lib/
COPY --chown=185 --from=build /app/target/quarkus-app/*.jar .
COPY --chown=185 --from=build /app/target/quarkus-app/app/ app/
COPY --chown=185 --from=build /app/target/quarkus-app/quarkus/ quarkus/

EXPOSE 8080
USER 185

ENTRYPOINT [ "java", "-jar", "quarkus-run.jar", "-Dquarkus.http.host=0.0.0.0", "-Djava.util.logging.manager=org.jboss.logmanager.LogManager", "-XX:+UnlockExperimentalVMOptions", "-XX:+EnableJVMCI" ]
