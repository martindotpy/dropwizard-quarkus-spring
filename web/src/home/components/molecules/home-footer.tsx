import { ThemeOptionSelect } from "@/core/components/atoms/theme-option-select"
import { OpenapiLink } from "@/home/components/atoms/openapi-link"
import { RepositoryLink } from "@/home/components/atoms/repository-link"

// Component
export function HomeFooter() {
  return (
    <footer className="not-prose flex w-full items-center justify-center">
      <OpenapiLink />
      <ThemeOptionSelect side="bottom" />
      <RepositoryLink />
    </footer>
  )
}
