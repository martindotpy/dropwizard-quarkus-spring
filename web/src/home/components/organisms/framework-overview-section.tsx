import { frameworkComparisonOverview } from "@/home/components/organisms/data/framework-section-data"

export function FrameworkOverviewSection() {
  const { matrix, scope, sharedRuntimeSampler } = frameworkComparisonOverview

  return (
    <section className="not-prose border-border/80 bg-card/45 mt-8 space-y-4 rounded-2xl border p-4 shadow-sm backdrop-blur-sm lg:p-5">
      <header className="space-y-2">
        <p className="text-muted-foreground text-xs font-semibold tracking-[0.08em] uppercase">
          Vision transversal
        </p>
        <h2 className="font-mono text-lg font-semibold tracking-tight lg:text-xl">
          Misma API JSON, diferentes decisiones de implementacion
        </h2>
        <p className="text-muted-foreground max-w-3xl text-sm leading-relaxed">
          Esta vista resume como cada stack resuelve HTTP, DI, persistencia, SSE
          y errores sin modificar contratos ni agregar endpoints nuevos.
        </p>
      </header>

      <div className="grid gap-3 lg:grid-cols-3">
        <article className="border-border/70 bg-background/85 rounded-xl border p-3">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            Objetivo
          </h3>
          <p className="mt-2 text-sm leading-relaxed">{scope.objective}</p>
        </article>

        <article className="border-border/70 bg-background/85 rounded-xl border p-3">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            Endpoint estatico
          </h3>
          <code className="bg-card mt-2 block rounded-md px-2 py-1 text-xs leading-relaxed break-all">
            {scope.staticEndpoint}
          </code>
        </article>

        <article className="border-border/70 bg-background/85 rounded-xl border p-3">
          <h3 className="font-mono text-xs font-semibold tracking-[0.06em] uppercase">
            Endpoint SSE
          </h3>
          <code className="bg-card mt-2 block rounded-md px-2 py-1 text-xs leading-relaxed break-all">
            {scope.streamingEndpoint}
          </code>
        </article>
      </div>

      <div className="grid gap-3 lg:grid-cols-2">
        {matrix.map((row) => (
          <article
            key={row.concern}
            className="border-border/70 bg-background/85 rounded-xl border p-3"
          >
            <h3 className="font-mono text-sm font-semibold tracking-tight">
              {row.concern}
            </h3>

            <dl className="mt-3 space-y-2 text-xs leading-relaxed">
              <div className="grid grid-cols-[6.5rem_1fr] gap-2">
                <dt className="text-muted-foreground">Dropwizard</dt>
                <dd>{row.dropwizard}</dd>
              </div>
              <div className="grid grid-cols-[6.5rem_1fr] gap-2">
                <dt className="text-muted-foreground">Quarkus</dt>
                <dd>{row.quarkus}</dd>
              </div>
              <div className="grid grid-cols-[6.5rem_1fr] gap-2">
                <dt className="text-muted-foreground">Spring</dt>
                <dd>{row.spring}</dd>
              </div>
            </dl>
          </article>
        ))}
      </div>

      <p className="text-muted-foreground border-border/70 bg-background/70 rounded-xl border p-3 text-xs leading-relaxed">
        {sharedRuntimeSampler}
      </p>
    </section>
  )
}
