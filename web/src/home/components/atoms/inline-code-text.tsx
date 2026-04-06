import { cn } from "@/core/lib/tailwind"

type InlineCodeTextProps = {
  text: string
  codeClassName?: string
}

export function InlineCodeText({ text, codeClassName }: InlineCodeTextProps) {
  const segments = text.split(/(`[^`]+`)/g).filter(Boolean)

  return (
    <>
      {segments.map((segment, index) => {
        const isCode = segment.startsWith("`") && segment.endsWith("`")

        if (!isCode) {
          return <span key={`${segment}-${index}`}>{segment}</span>
        }

        return (
          <code
            key={`${segment}-${index}`}
            className={cn(
              "border-border/60 bg-card mx-0.5 rounded border px-0.5 font-mono text-[0.72rem]",
              codeClassName
            )}
          >
            {segment.slice(1, -1)}
          </code>
        )
      })}
    </>
  )
}
