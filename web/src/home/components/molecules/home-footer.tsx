import { ThemeOptionSelect } from "@/core/components/atoms/theme-option-select"
import { buttonVariants } from "@/core/components/ui/button"
import { TbApi, TbBrandGithub } from "react-icons/tb"

// Component
export function HomeFooter() {
  return (
    <footer className="not-prose flex w-full items-center justify-center">
      <a
        href="/docs"
        className={buttonVariants({ variant: "ghost", size: "icon-lg" })}
      >
        <TbApi className="size-5" />
      </a>

      <ThemeOptionSelect />

      <a
        href="https://github.com/martindotpy/dropwizard-quarkus-spring"
        className={buttonVariants({ variant: "ghost", size: "icon-lg" })}
        rel="noopener noreferrer"
      >
        <TbBrandGithub className="size-5" />
      </a>
    </footer>
  )
}
