import { buttonVariants } from "@/core/components/ui/button"
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/core/components/ui/tooltip"
import { TbBrandGithub } from "react-icons/tb"

// Component
export function RepositoryLink() {
  return (
    <Tooltip>
      <TooltipContent>Repositorio del proyecto en GitHub</TooltipContent>
      <TooltipTrigger
        render={
          <a
            href="https://github.com/martindotpy/dropwizard-quarkus-spring"
            className={buttonVariants({
              variant: "ghost",
              size: "icon-lg",
            })}
            rel="noopener noreferrer"
          >
            <TbBrandGithub className="size-5" />
          </a>
        }
      />
    </Tooltip>
  )
}
