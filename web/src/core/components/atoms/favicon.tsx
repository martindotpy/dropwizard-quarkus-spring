import favicon from "@/assets/svg/favicon.svg"

// Component
type FaviconProps = React.ComponentProps<"img">

export function Favicon(props: FaviconProps) {
  return <img {...favicon} {...props} />
}
