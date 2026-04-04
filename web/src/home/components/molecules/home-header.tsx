import { ThemeOptionSelect } from "@/core/components/atoms/theme-option-select"
import { buttonVariants } from "@/core/components/ui/button"
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/core/components/ui/tooltip"
import { TbApi, TbBrandGithub } from "react-icons/tb"

// Component
export function HomeHeader() {
  return (
    <header className="mt-12">
      <h1>
        <code>Dropwizard, Quarkus y Spring</code>
      </h1>

      <div className="not-prose flex items-center justify-center">
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

        <ThemeOptionSelect side="bottom" />

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
      </div>

      <p>
        Análisis comparativo de Dropwizard, Quarkus y Spring Boot, enfocado en
        su rendimiento, facilidad de uso y características para el desarrollo de
        aplicaciones en la nube. Puedes encontrar arriba tanto la documentación
        de los endpoints de la API como el código fuente del proyecto.
      </p>
    </header>
  )
}
