import { ThemeOptionSelect } from "@/core/components/atoms/theme-option-select"
import { OpenapiLink } from "@/home/components/atoms/openapi-link"
import { RepositoryLink } from "@/home/components/atoms/repository-link"

// Component
export function HomeHeader() {
  return (
    <header className="mt-12">
      <h1>
        <code>Dropwizard, Quarkus y Spring</code>
      </h1>

      <div className="not-prose flex items-center justify-center">
        <OpenapiLink />
        <ThemeOptionSelect side="bottom" />
        <RepositoryLink />
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
