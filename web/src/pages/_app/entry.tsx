import { isSsr } from "@/core/configuration/app-configuration"
import { type AppRouter, createAppRouter } from "@/pages/_app/router"
import { RouterProvider } from "@tanstack/react-router"
import { RouterClient } from "@tanstack/react-router/ssr/client"
import type { ReactNode } from "react"

// Client router
let clientRouter: AppRouter | undefined

if (!isSsr) {
  clientRouter = createAppRouter()
}

// Component
interface AppEntryProps {
  getServerRouter?: () => AppRouter
  clientOnly?: boolean
  quarkus?: ReactNode
}

export function AppEntry({
  getServerRouter,
  clientOnly,
}: AppEntryProps) {
  return clientOnly ? (
    <RouterProvider router={clientRouter!} />
  ) : isSsr ? (
    <RouterProvider router={getServerRouter!()} />
  ) : (
    <RouterClient router={clientRouter!} />
  )
}
