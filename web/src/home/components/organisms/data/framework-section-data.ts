import {
  getApiDropwizardCloudMetricsLive,
  getApiQuarkusCloudMetricsLive,
  getApiSpringCloudMetricsLive,
} from "@/api/client"
import {
  getApiDropwizardCloudMetricsInfoOptions,
  getApiQuarkusCloudMetricsInfoOptions,
  getApiSpringCloudMetricsInfoOptions,
} from "@/api/client/@tanstack/react-query.gen"

export type Framework = "dropwizard" | "quarkus" | "spring"

export type FrameworkSection = Record<Framework, object>

type FrameworkJsonGuide = {
  infoEndpoint: string
  infoFields: Array<{
    path: string
    meaning: string
    howToRead: string
  }>
  liveEndpoint: string
  liveFields: Array<{
    path: string
    meaning: string
    howToRead: string
  }>
}

function createJsonGuide(
  framework: Framework,
  startupReadySource: string
): FrameworkJsonGuide {
  return {
    infoEndpoint: `GET /api/${framework}/cloud/metrics/info`,
    infoFields: [
      {
        path: "framework",
        meaning: "Identificador canonico del stack backend.",
        howToRead:
          "Sirve para validar que el gateway responde desde el framework esperado.",
      },
      {
        path: "runtimeMode",
        meaning: "Modo de ejecucion detectado por la `JVM` o imagen nativa.",
        howToRead:
          "Permite comparar comportamiento `JVM` vs `native` en el mismo contrato JSON.",
      },
      {
        path: "startupReadyMs",
        meaning: "Tiempo de arranque reportado por el backend.",
        howToRead: startupReadySource,
      },
      {
        path: "images[].platforms[]",
        meaning: "Resumen multi-arquitectura de imagenes GHCR.",
        howToRead:
          "Cada item expone `digest` y `size` para auditar huella de despliegue.",
      },
    ],
    liveEndpoint: `GET /api/${framework}/cloud/metrics/live (SSE)`,
    liveFields: [
      {
        path: "collectedAt",
        meaning: "Marca temporal de muestreo.",
        howToRead:
          "Se actualiza en cada evento y sirve para medir frescura de stream.",
      },
      {
        path: "startupMetrics.processUptimeSeconds",
        meaning: "Uptime acumulado del proceso.",
        howToRead:
          "Sube linealmente; si reinicia a cero hubo redeploy o crash.",
      },
      {
        path: "resourceSnapshot.processCpuLoad",
        meaning: "Carga de CPU del proceso (0.0 a 1.0).",
        howToRead:
          "Multiplicar por 100 para porcentaje visible en dashboard tecnico.",
      },
      {
        path: "resourceSnapshot.usedMemoryMiB",
        meaning: "Memoria heap usada en MiB.",
        howToRead:
          "Comparar contra totalMemoryBytes para entender presion de memoria.",
      },
      {
        path: "resourceSnapshot.gcCollectionCount",
        meaning: "Cantidad acumulada de ciclos GC.",
        howToRead:
          "Saltos abruptos junto a CPU alta suelen indicar stress de asignacion.",
      },
    ],
  }
}

export const frameworkComparisonOverview = {
  scope: {
    objective:
      "Comparar implementaciones con el mismo contrato JSON, sin agregar endpoints nuevos.",
    staticEndpoint: "GET /api/{framework}/cloud/metrics/info",
    streamingEndpoint: "GET /api/{framework}/cloud/metrics/live",
  },
  matrix: [
    {
      concern: "HTTP layer",
      dropwizard: "`JAX-RS` (`Jersey`)",
      quarkus: "`RESTEasy Reactive`",
      spring: "`Spring Web MVC`",
    },
    {
      concern: "Dependency injection",
      dropwizard: "Wiring manual en `DropwizardApplication.run()`",
      quarkus: "`CDI` (`@Inject`, `@ApplicationScoped`)",
      spring: "IoC container (`@Service`, `@Configuration`)",
    },
    {
      concern: "Mongo abstraction",
      dropwizard: "`MongoCollection` explicita",
      quarkus: "`PanacheMongoRepository`",
      spring: "`Spring Data MongoRepository`",
    },
    {
      concern: "SSE implementation",
      dropwizard: "`SseEventSink` + virtual thread",
      quarkus: "`Mutiny Multi` tick stream",
      spring: "`Reactor Flux<ServerSentEvent>`",
    },
    {
      concern: "OpenAPI publication",
      dropwizard: "Controlador dedicado que sirve `openapi.json`",
      quarkus: "`SmallRye OpenAPI` path configurado",
      spring: "`springdoc api-docs.path` configurado",
    },
    {
      concern: "Error contract",
      dropwizard: "`ErrorMessage` + `ValidationErrorMessage`",
      quarkus: "`application/problem+json`",
      spring: "`ProblemDetail` + `ResponseStatusException`",
    },
  ],
  sharedRuntimeSampler:
    "`shared/cloud/model/ServiceSnapshotFactory.java` genera `startupMetrics` y `resourceSnapshot` para todos.",
}

export const frameworkSectionData = {
  dropwizard: {
    title: "Dropwizard",
    serviceInformationQueryOptions: () =>
      getApiDropwizardCloudMetricsInfoOptions(),
    getLiveMetricsSse: getApiDropwizardCloudMetricsLive,
    implementation: {
      profile:
        "Stack centrado en wiring manual, ideal para observar cada dependencia de infraestructura sin perder contrato `JSON`.",
      architecture: {
        httpLayer: "`JAX-RS`/`Jersey` sobre Dropwizard",
        dependencyInjection:
          "Instanciacion manual de controladores, repositorios y casos de uso en `DropwizardApplication.run()`.",
        persistence:
          "Repositorio concreto con `MongoCollection<Note>` y operaciones CRUD explicitas.",
        sseStreaming:
          "`SseEventSink` + `OutboundSseEvent` emitido en virtual thread cada 1 segundo.",
        openapi:
          "`OpenApiController` expone `/api/dropwizard/openapi.json` desde recurso generado en build.",
        errorModel:
          "`ErrorMessage` y `ValidationErrorMessage` con respuestas `422` y `404`.",
        startupReadyMetric:
          "`ServerLifecycleListener` toma uptime del server cuando queda listo.",
        ghcrClient:
          "`jakarta.ws.rs.client.Client` para token y manifest sin capa extra de abstraccion.",
      },
      notesCrudFlow: [
        "Controller `JAX-RS` -> Port de aplicacion",
        "Use case -> `NoteMongoRepository`",
        "Repository -> `MongoCollection`",
      ],
      references: [
        "dropwizard/src/main/java/dev/martindotpy/dropwizardquarkusspring/dropwizard/DropwizardApplication.java",
        "dropwizard/src/main/java/dev/martindotpy/dropwizardquarkusspring/dropwizard/core/adapter/controller/MetricsController.java",
        "dropwizard/src/main/java/dev/martindotpy/dropwizardquarkusspring/dropwizard/note/adapter/controller/NoteController.java",
        "dropwizard/src/main/java/dev/martindotpy/dropwizardquarkusspring/dropwizard/core/adapter/repository/NoteMongoRepository.java",
      ],
    },
    jsonGuide: createJsonGuide(
      "dropwizard",
      "Fuente: `server.getUptimeMillis()` desde `ServerLifecycleListener`."
    ),
  },
  quarkus: {
    title: "Quarkus",
    serviceInformationQueryOptions: () =>
      getApiQuarkusCloudMetricsInfoOptions(),
    getLiveMetricsSse: getApiQuarkusCloudMetricsLive,
    implementation: {
      profile:
        "Stack con foco cloud-native y extensiones reactivas, manteniendo el mismo contrato `JSON`.",
      architecture: {
        httpLayer: "`RESTEasy Reactive`",
        dependencyInjection:
          "`CDI` con `@Inject` y beans `@ApplicationScoped`.",
        persistence:
          "`PanacheMongoRepository` para CRUD con menor codigo de infraestructura.",
        sseStreaming:
          "`Mutiny Multi.createFrom().ticks().every(...)` para publicar eventos metrics.",
        openapi:
          "`SmallRye OpenAPI` en `/api/quarkus/openapi.json` con filtro `ProblemDetails`.",
        errorModel: "Problemas HTTP en `application/problem+json`.",
        startupReadyMetric:
          "`StartupEvent` + lectura de `io.quarkus.bootstrap.runner.Timing` (fallback incluido).",
        ghcrClient:
          "`MicroProfile Rest Client` (`@RegisterRestClient`) para token y manifest.",
      },
      notesCrudFlow: [
        "Controller `RESTEasy` -> Port de aplicacion",
        "Use case -> `NoteRepository` (Panache)",
        "Panache -> `MongoDB`",
      ],
      references: [
        "quarkus/src/main/java/dev/martindotpy/dropwizardquarkusspring/quarkus/core/adapter/controller/MetricsController.java",
        "quarkus/src/main/java/dev/martindotpy/dropwizardquarkusspring/quarkus/note/adapter/controller/NoteController.java",
        "quarkus/src/main/java/dev/martindotpy/dropwizardquarkusspring/quarkus/note/domain/repository/NoteRepository.java",
        "quarkus/src/main/java/dev/martindotpy/dropwizardquarkusspring/quarkus/core/adapter/client/GhcrManifestClient.java",
      ],
    },
    jsonGuide: createJsonGuide(
      "quarkus",
      "Fuente: `StartupEvent` + `Timing.bootStartTime` via reflection."
    ),
  },
  spring: {
    title: "Spring",
    serviceInformationQueryOptions: () => getApiSpringCloudMetricsInfoOptions(),
    getLiveMetricsSse: getApiSpringCloudMetricsLive,
    implementation: {
      profile:
        "Stack empresarial basado en IoC + Spring Data, con salida de metricas equivalente sobre `JSON`.",
      architecture: {
        httpLayer: "`@RestController` + `Spring MVC` annotations",
        dependencyInjection:
          "Inyeccion por constructor con beans del contenedor `Spring`.",
        persistence: "`MongoRepository<ObjectId>` de `Spring Data MongoDB`.",
        sseStreaming:
          "`Flux.interval(...)` que emite `ServerSentEvent<ServiceLiveMetrics>`.",
        openapi: "`springdoc api-docs.path=/api/spring/openapi.json`.",
        errorModel:
          "`ProblemDetail` y `ResponseStatusException` para errores de validacion y negocio.",
        startupReadyMetric:
          "`ApplicationReadyEvent.getTimeTaken()` capturado por `StartupReadyTracker`.",
        ghcrClient:
          "`RestClient` + `HttpServiceProxyFactory` para clientes tipados.",
      },
      notesCrudFlow: [
        "Controller `MVC` -> Port de aplicacion",
        "Use case -> `NoteRepository` (Spring Data)",
        "`MongoRepository` -> `MongoDB`",
      ],
      references: [
        "spring/src/main/java/dev/martindotpy/dropwizardquarkusspring/spring/core/adapter/controller/MetricsController.java",
        "spring/src/main/java/dev/martindotpy/dropwizardquarkusspring/spring/note/adapter/controller/NoteController.java",
        "spring/src/main/java/dev/martindotpy/dropwizardquarkusspring/spring/note/domain/repository/NoteRepository.java",
        "spring/src/main/java/dev/martindotpy/dropwizardquarkusspring/spring/core/adapter/configuration/StartupReadyTracker.java",
      ],
    },
    jsonGuide: createJsonGuide(
      "spring",
      "Fuente: `ApplicationReadyEvent.timeTaken` en `StartupReadyTracker`."
    ),
  },
} satisfies FrameworkSection
