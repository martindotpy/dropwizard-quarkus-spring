ARG TZ=America/Lima

FROM eclipse-temurin:25-jdk-ubi10-minimal AS build

ARG TZ

ENV TZ=${TZ}

# Workspace packages
WORKDIR /app

# Maven and dependencies
COPY .mvn/ .mvn/
COPY --chmod=0755 mvnw ./

COPY pom.xml ./
COPY shared/pom.xml ./shared/pom.xml
COPY dropwizard/pom.xml ./dropwizard/pom.xml
COPY quarkus/pom.xml ./quarkus/pom.xml
COPY spring/pom.xml ./spring/pom.xml

RUN ./mvnw -B dependency:go-offline

# Build
COPY shared/src shared/src
COPY spring/src spring/src

RUN ./mvnw -pl shared -am install -DskipTests -B
RUN ./mvnw -pl spring -am package -DskipTests -B


FROM gcr.io/distroless/java25 AS runtime

# Add wget for ready endpoint healthcheck
COPY --from=busybox /lib/* /lib/
COPY --from=busybox /lib64/* /lib64/
COPY --from=busybox /bin/wget /bin/

WORKDIR /app

ARG TZ

ENV TZ=${TZ}

COPY --chown=185 --from=build /app/spring/target/*.jar .

EXPOSE 8080
USER 185

ENTRYPOINT [ "java", "-jar", "spring-1.0.0-SNAPSHOT.jar", "-XX:+UnlockExperimentalVMOptions", "-XX:+EnableJVMCI"]

HEALTHCHECK --interval=120s --timeout=5s --start-period=5s \
    CMD ["/bin/wget", "--spider", "--timeout=5", "http://localhost:8082/hello"]
