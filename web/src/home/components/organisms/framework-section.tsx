import type { frameworkSectionData } from "@/home/components/organisms/data/framework-section-data"
import { useQuery } from "@tanstack/react-query"
import { useEffect, useState } from "react"

// Component
type FrameworkSectionProps =
  typeof frameworkSectionData extends Record<string, infer V> ? V : never

export function FrameworkSection({
  title,
  serviceInformationQueryOptions,
  getLiveMetricsSse,
}: FrameworkSectionProps) {
  // Service information query
  const { data, isFetching } = useQuery({
    ...serviceInformationQueryOptions(),
    initialData: {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-expect-error
      detail: "Cargando datos...",
    },
  })

  // Live metrics
  const [liveMetrics, setLiveMetrics] = useState(
    JSON.stringify(
      {
        detail: "Esperando datos SSE...",
      },
      null,
      2
    )
  )
  const [isSseConnected, setIsSseConnected] = useState(false)

  useEffect(() => {
    ;(async () => {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-expect-error
      const { stream } = await getLiveMetricsSse({
        onSseEvent: () => {
          setIsSseConnected(true)
        },
      })

      for await (const chunk of stream) {
        setLiveMetrics(JSON.stringify(chunk, null, 2))
      }
    })()
  }, [getLiveMetricsSse])

  return (
    <section>
      <h2>
        <code>{title}</code>
      </h2>

      <h3>Información</h3>

      <pre
        className="bg-card text-card-foreground data-[loading=true]:animate-pulse lg:min-h-260.5"
        data-loading={isFetching}
      >
        <code>{JSON.stringify(data, null, 2)}</code>
      </pre>

      <h3>Estadísticas en vivo</h3>

      <pre
        className="bg-card text-card-foreground data-[loading=true]:animate-pulse lg:min-h-129"
        data-loading={!isSseConnected}
      >
        <code>{liveMetrics}</code>
      </pre>
    </section>
  )
}
