import { isDev } from "@/core/configuration/app-configuration"
import { tanstackQueryDevtools } from "@/core/devtools/tanstack-query-devtools"
import { tanstackRouterDevtools } from "@/core/devtools/tanstack-router-devtools"
import { TanStackDevtools } from "@tanstack/react-devtools"

// Component
export function Devtools() {
  return (
    isDev && (
      <TanStackDevtools
        config={{
          position: "bottom-right",
        }}
        plugins={[tanstackRouterDevtools, tanstackQueryDevtools]}
      />
    )
  )
}
