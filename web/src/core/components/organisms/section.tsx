import { sectionClassName } from "@/core/components/organisms/styles/section-styles"
import { cn } from "@/core/lib/tailwind"

// Component
type SectionProps = React.HTMLAttributes<HTMLElement>

export function Section({ children, className, ...props }: SectionProps) {
  return (
    <section className={cn(sectionClassName, className)} {...props}>
      {children}
    </section>
  )
}
