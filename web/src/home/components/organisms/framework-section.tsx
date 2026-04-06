import type {
  OpenapiServiceComparison,
  OpenapiServiceLiveMetrics,
} from "@/api/client"
import type { frameworkSectionData } from "@/home/components/organisms/data/framework-section-data"
import { useQuery } from "@tanstack/react-query"
import { useEffect, useState } from "react"

type FrameworkSectionProps =
  typeof frameworkSectionData extends Record<string, infer V> ? V : never

const architectureLabels = {
  httpLayer: "HTTP layer",
  dependencyInjection: "Dependency injection",
  persistence: "Persistence",
  sseStreaming: "SSE streaming",
  openapi: "OpenAPI",
  errorModel: "Error model",
  startupReadyMetric: "Startup metric source",
  ghcrClient: "GHCR client",
} as const

function toPrettyJson(value: unknown): string {
  return JSON.stringify(value, null, 2)
}

function formatMs(value?: number): string {
  if (typeof value !== "number" || Number.isNaN(value)) {
    return "N/A"
  }

  return `${Math.round(value).toLocaleString("es-ES")} ms`
}

function formatPercent(value?: number): string {
  if (typeof value !== "number" || Number.isNaN(value) || value < 0) {
    return "N/A"
  }

  return `${(value * 100).toFixed(1)}%`
}

function formatInteger(value?: number): string {
  if (typeof value !== "number" || Number.isNaN(value) || value < 0) {
    return "N/A"
  }

  return Math.round(value).toLocaleString("es-ES")
}

function formatBytes(value?: number): string {
  if (typeof value !== "number" || Number.isNaN(value) || value < 0) {
    return "N/A"
  }

  const units = ["B", "KB", "MB", "GB", "TB"]
  let normalized = value
  let unitIndex = 0

  while (normalized >= 1024 && unitIndex < units.length - 1) {
    normalized /= 1024
    unitIndex += 1
  }

  return `${normalized.toFixed(unitIndex === 0 ? 0 : 1)} ${units[unitIndex]}`
}

function formatIsoDate(value?: string): string {
  if (!value) {
    return "N/A"
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  return date.toLocaleString("es-ES", { hour12: false })
}

function formatTimestamp(value?: number): string {
  if (typeof value !== "number" || Number.isNaN(value) || value <= 0) {
    return "N/A"
  }

  return new Date(value).toLocaleString("es-ES", { hour12: false })
}

export function FrameworkSection({
  title,
  serviceInformationQueryOptions,
  getLiveMetricsSse,
  implementation,
  jsonGuide,
}: FrameworkSectionProps) {
  const { data, error, isError, isFetching, isSuccess, dataUpdatedAt } =
    useQuery(serviceInformationQueryOptions())

  const frameworkInfo = data as OpenapiServiceComparison | undefined

  const [liveMetrics, setLiveMetrics] = useState<OpenapiServiceLiveMetrics>()
  const [liveMetricsJson, setLiveMetricsJson] = useState(
    toPrettyJson({ detail: "Esperando datos SSE..." })
  )
  const [sseStatus, setSseStatus] = useState<
    "connecting" | "connected" | "error"
  >("connecting")
  const [sseError, setSseError] = useState<string>()

  useEffect(() => {
    const abortController = new AbortController()
    let isMounted = true

    ;(async () => {
      try {
        const { stream } = await getLiveMetricsSse({
          signal: abortController.signal,
          onSseEvent: () => {
            if (!isMounted) {
              return
            }

            setSseStatus("connected")
            setSseError(undefined)
          },
          onSseError: (streamError) => {
            if (!isMounted || abortController.signal.aborted) {
              return
            }

            const message =
              streamError instanceof Error
                ? streamError.message
                : "No fue posible leer eventos SSE."

            setSseStatus("error")
            setSseError(message)
          },
        })

        for await (const chunk of stream) {
          if (!isMounted || abortController.signal.aborted) {
            break
          }

          const payload = chunk as OpenapiServiceLiveMetrics
          setLiveMetrics(payload)
          setLiveMetricsJson(toPrettyJson(chunk))
          setSseError(undefined)
        }
      } catch (streamError) {
        if (!isMounted || abortController.signal.aborted) {
          return
        }

        const message =
          streamError instanceof Error
            ? streamError.message
            : "No fue posible inicializar SSE."

        setSseStatus("error")
        setSseError(message)
      }
    })()

    return () => {
      isMounted = false
      abortController.abort()
    }
  }, [getLiveMetricsSse])

  const resources = liveMetrics?.resourceSnapshot
  const startupMetrics = liveMetrics?.startupMetrics
  const imageCount = frameworkInfo?.images?.length ?? 0
  const platformCount =
    frameworkInfo?.images?.reduce(
      (accumulator, image) => accumulator + (image.platforms?.length ?? 0),
      0
    ) ?? 0
  const heapUtilization =
    typeof resources?.usedMemoryBytes === "number" &&
    typeof resources.totalMemoryBytes === "number" &&
    resources.totalMemoryBytes > 0
      ? resources.usedMemoryBytes / resources.totalMemoryBytes
      : undefined

  const staticJson = isSuccess
    ? toPrettyJson(frameworkInfo)
    : toPrettyJson({ detail: "Cargando datos..." })

  const sseStatusLabel =
    sseStatus === "connected"
      ? "SSE activo"
      : sseStatus === "error"
        ? "SSE con error"
        : "SSE conectando"

  return (
    <section className="not-prose border-border/80 bg-card/45 min-w-0 space-y-4 rounded-2xl border p-4 shadow-sm backdrop-blur-sm lg:p-5">
      <header className="space-y-2">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <h2 className="font-mono text-lg font-semibold tracking-tight lg:text-xl">
            {title}
          </h2>

          <div className="flex flex-wrap items-center gap-2 text-xs">
            <span className="border-border bg-background/85 rounded-md border px-2 py-1 font-mono">
              {frameworkInfo?.runtimeMode ?? "runtime N/A"}
            </span>
            <span className="border-border bg-background/85 rounded-md border px-2 py-1 font-mono">
              {sseStatusLabel}
            </span>
          </div>
        </div>

        <p className="text-muted-foreground text-sm leading-relaxed">
          {implementation.profile}
        </p>
      </header>

      <div className="grid gap-3 xl:grid-cols-2 *:min-w-0">
        <article className="border-border/70 bg-background/85 rounded-xl border p-3">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            Metricas clave
          </h3>

          <dl className="mt-3 space-y-2 text-xs leading-relaxed">
            <div className="grid grid-cols-[11rem_1fr] gap-2">
              <dt className="text-muted-foreground">Startup ready</dt>
              <dd className="font-mono">
                {formatMs(frameworkInfo?.startupReadyMs)}
              </dd>
            </div>
            <div className="grid grid-cols-[11rem_1fr] gap-2">
              <dt className="text-muted-foreground">Process uptime</dt>
              <dd className="font-mono">
                {formatInteger(startupMetrics?.processUptimeSeconds)} s
              </dd>
            </div>
            <div className="grid grid-cols-[11rem_1fr] gap-2">
              <dt className="text-muted-foreground">Process CPU</dt>
              <dd className="font-mono">
                {formatPercent(resources?.processCpuLoad)}
              </dd>
            </div>
            <div className="grid grid-cols-[11rem_1fr] gap-2">
              <dt className="text-muted-foreground">Heap used</dt>
              <dd className="font-mono">
                {formatBytes(resources?.usedMemoryBytes)}
              </dd>
            </div>
            <div className="grid grid-cols-[11rem_1fr] gap-2">
              <dt className="text-muted-foreground">Heap utilization</dt>
              <dd className="font-mono">{formatPercent(heapUtilization)}</dd>
            </div>
            <div className="grid grid-cols-[11rem_1fr] gap-2">
              <dt className="text-muted-foreground">GC count</dt>
              <dd className="font-mono">
                {formatInteger(resources?.gcCollectionCount)}
              </dd>
            </div>
            <div className="grid grid-cols-[11rem_1fr] gap-2">
              <dt className="text-muted-foreground">Images/platforms</dt>
              <dd className="font-mono">
                {imageCount.toLocaleString("es-ES")}/
                {platformCount.toLocaleString("es-ES")}
              </dd>
            </div>
            <div className="grid grid-cols-[11rem_1fr] gap-2">
              <dt className="text-muted-foreground">Live sampled at</dt>
              <dd className="font-mono">
                {formatIsoDate(liveMetrics?.collectedAt)}
              </dd>
            </div>
          </dl>
        </article>

        <article className="border-border/70 bg-background/85 rounded-xl border p-3">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            Arquitectura aplicada
          </h3>

          <dl className="mt-3 space-y-2 text-xs leading-relaxed">
            {Object.entries(implementation.architecture).map(([key, value]) => (
              <div key={key} className="grid grid-cols-[11rem_1fr] gap-2">
                <dt className="text-muted-foreground">
                  {architectureLabels[key as keyof typeof architectureLabels]}
                </dt>
                <dd>{value}</dd>
              </div>
            ))}
          </dl>
        </article>
      </div>

      <div className="grid gap-3 xl:grid-cols-2 *:min-w-0">
        <article className="border-border/70 bg-background/85 rounded-xl border p-3">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            Flujo CRUD de notas
          </h3>

          <ol className="mt-3 space-y-2 text-xs leading-relaxed">
            {implementation.notesCrudFlow.map((step) => (
              <li
                key={step}
                className="border-border/60 bg-card/65 rounded-md border px-2 py-1.5"
              >
                {step}
              </li>
            ))}
          </ol>
        </article>

        <article className="border-border/70 bg-background/85 rounded-xl border p-3">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            Referencias del codigo
          </h3>

          <ul className="mt-3 space-y-2 text-xs leading-relaxed">
            {implementation.references.map((reference) => (
              <li
                key={reference}
                className="border-border/60 bg-card/65 rounded-md border px-2 py-1.5"
              >
                <code className="break-all">{reference}</code>
              </li>
            ))}
          </ul>
        </article>
      </div>

      <article className="border-border/70 bg-background/85 rounded-xl border p-3">
        <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
          Guia de lectura JSON
        </h3>

        <div className="mt-3 grid gap-3 xl:grid-cols-2 *:min-w-0">
          <div className="space-y-2">
            <h4 className="text-xs font-semibold tracking-wide">
              Info endpoint
            </h4>
            <code className="bg-card block rounded-md px-2 py-1 text-xs">
              {jsonGuide.infoEndpoint}
            </code>
            <ul className="space-y-2 text-xs leading-relaxed">
              {jsonGuide.infoFields.map((field) => (
                <li
                  key={field.path}
                  className="border-border/60 bg-card/65 rounded-md border px-2 py-1.5"
                >
                  <p className="font-mono">{field.path}</p>
                  <p className="text-muted-foreground mt-1">{field.meaning}</p>
                  <p className="mt-1">{field.howToRead}</p>
                </li>
              ))}
            </ul>
          </div>

          <div className="space-y-2">
            <h4 className="text-xs font-semibold tracking-wide">
              Live endpoint
            </h4>
            <code className="bg-card block rounded-md px-2 py-1 text-xs">
              {jsonGuide.liveEndpoint}
            </code>
            <ul className="space-y-2 text-xs leading-relaxed">
              {jsonGuide.liveFields.map((field) => (
                <li
                  key={field.path}
                  className="border-border/60 bg-card/65 rounded-md border px-2 py-1.5"
                >
                  <p className="font-mono">{field.path}</p>
                  <p className="text-muted-foreground mt-1">{field.meaning}</p>
                  <p className="mt-1">{field.howToRead}</p>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </article>

      <div className="grid gap-3 xl:grid-cols-2 *:min-w-0">
        <article className="space-y-2">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            JSON estatico
          </h3>

          <pre
            className="text-card-foreground border-border/70 bg-card max-h-128 w-full max-w-full overflow-x-auto overflow-y-auto rounded-xl border p-3 text-xs leading-relaxed data-[loading=true]:animate-pulse"
            data-loading={isFetching}
          >
            <code className="block min-w-max">{staticJson}</code>
          </pre>
          {isError ? (
            <p className="text-destructive text-xs leading-relaxed">
              Error al consultar info: {String(error)}
            </p>
          ) : null}
          <p className="text-muted-foreground text-[11px] leading-relaxed">
            Ultima actualizacion: {formatTimestamp(dataUpdatedAt || undefined)}
          </p>
        </article>

        <article className="space-y-2">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            JSON en vivo (SSE)
          </h3>

          <pre
            className="text-card-foreground border-border/70 bg-card max-h-128 w-full max-w-full overflow-x-auto overflow-y-auto rounded-xl border p-3 text-xs leading-relaxed data-[loading=true]:animate-pulse"
            data-loading={sseStatus === "connecting"}
          >
            <code className="block min-w-max">{liveMetricsJson}</code>
          </pre>

          {sseError ? (
            <p className="text-destructive text-xs leading-relaxed">
              Error SSE: {sseError}
            </p>
          ) : null}
        </article>
      </div>
    </section>
  )
}
