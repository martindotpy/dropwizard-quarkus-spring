import { HomeFooter } from "@/home/components/molecules/home-footer"
import { HomeHeader } from "@/home/components/molecules/home-header"
import { createFileRoute } from "@tanstack/react-router"

// Route
export const Route = createFileRoute("/")({
  component: IndexComponent,
})

function IndexComponent() {
  return (
    <>
      <div className="prose prose-h2:text-center dark:prose-invert mx-auto w-full max-w-7xl p-5">
        <HomeHeader />

        <div className="grid grid-cols-1 lg:grid-cols-3 lg:divide-x">
          <section>
            <h2>Dropwizard</h2>
          </section>
          <section>
            <h2>Quarkus</h2>
          </section>
          <section>
            <h2>Spring</h2>
          </section>
        </div>

        <HomeFooter />
      </div>
    </>
  )
}
