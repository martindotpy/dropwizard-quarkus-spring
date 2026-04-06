import { cn } from "@/core/lib/tailwind"
import { HomeFooter } from "@/home/components/molecules/home-footer"
import { HomeHeader } from "@/home/components/molecules/home-header"
import { frameworkSectionData } from "@/home/components/organisms/data/framework-section-data"
import { FrameworkOverviewSection } from "@/home/components/organisms/framework-overview-section"
import { FrameworkSection } from "@/home/components/organisms/framework-section"
import { createFileRoute } from "@tanstack/react-router"

// Route
export const Route = createFileRoute("/")({
  component: IndexComponent,
})

function IndexComponent() {
  return (
    <div className="relative overflow-hidden">

      <div className="prose prose-code:before:content-[''] prose-code:after:content-[''] dark:prose-invert relative mx-auto w-full max-w-7xl p-5">
        <HomeHeader />

        <FrameworkOverviewSection />

        <div className={cn("mt-6 grid grid-cols-1 gap-4")}>
          {Object.entries(frameworkSectionData).map(
            ([framework, frameworkData]) => (
              <FrameworkSection key={framework} {...frameworkData} />
            )
          )}
        </div>

        <HomeFooter />
      </div>
    </div>
  )
}
