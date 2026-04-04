import { buttonVariants } from "@/core/components/ui/button"
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/core/components/ui/tooltip"
import { TbApi } from "react-icons/tb"

// Component
export function OpenapiLink() {
  return (
    <Tooltip>
      <TooltipContent>Documentación OpenApi en Scalar</TooltipContent>
      <TooltipTrigger
        render={
          <a
            href="/docs"
            className={buttonVariants({
              variant: "ghost",
              size: "icon-lg",
            })}
          >
            <TbApi className="size-5" />
          </a>
        }
      />
    </Tooltip>
  )
}
