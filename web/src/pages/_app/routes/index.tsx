import { cn } from "@/core/lib/tailwind"
import { HomeFooter } from "@/home/components/molecules/home-footer"
import { HomeHeader } from "@/home/components/molecules/home-header"
import { frameworkSectionData } from "@/home/components/organisms/data/framework-section-data"
import { FrameworkSection } from "@/home/components/organisms/framework-section"
import { createFileRoute } from "@tanstack/react-router"

// Route
export const Route = createFileRoute("/")({
  component: IndexComponent,
})

function IndexComponent() {
  return (
    <>
      <div className="prose prose-code:before:content-[''] prose-code:after:content-[''] prose-h2:text-center dark:prose-invert mx-auto w-full max-w-7xl p-5">
        <HomeHeader />

        <div
          className={cn(
            "grid grid-cols-1 gap-3",
            "lg:grid-cols-3 lg:gap-0 lg:divide-x lg:*:px-5",
            "lg:[&>*:first-child]:pl-0 lg:[&>*:last-child]:pr-0"
          )}
        >
          {Object.entries(frameworkSectionData).map(
            ([framework, frameworkData]) => (
              <FrameworkSection key={framework} {...frameworkData} />
            )
          )}
        </div>

        <HomeFooter />
      </div>
    </>
  )
}
