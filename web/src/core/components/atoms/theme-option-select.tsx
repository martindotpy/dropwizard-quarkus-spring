import { Button } from "@/core/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/core/components/ui/dropdown-menu"
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/core/components/ui/tooltip"
import { useTheme } from "next-themes"
import type React from "react"

import { TbMoon, TbSun, TbSunMoon } from "react-icons/tb"

// Messages
const messages = {
  system: "Sistema",
  light: "Claro",
  dark: "Oscuro",
}

// Icons
const icons = {
  system: TbSunMoon,
  light: TbSun,
  dark: TbMoon,
}

// Component
type ThemeOptionSelectProps = React.ComponentProps<typeof DropdownMenuContent>

export function ThemeOptionSelect({ ...props }: ThemeOptionSelectProps) {
  // Theme
  const { theme = "system", themes, setTheme } = useTheme()

  // Icon
  const Icon = icons[theme as keyof typeof icons]

  return (
    <DropdownMenu>
      <Tooltip>
        <TooltipContent>Selecciona el tema de tu preferencia</TooltipContent>
        <TooltipTrigger
          render={
            <DropdownMenuTrigger
              render={
                <Button variant="ghost" size="icon-lg">
                  <Icon className="size-5" />
                </Button>
              }
            />
          }
        />
      </Tooltip>
      <DropdownMenuContent side="top" align="center" {...props}>
        {themes.map((themeOption) => (
          <DropdownMenuItem
            key={themeOption}
            onClick={() => setTheme(themeOption)}
            disabled={themeOption === theme}
          >
            {messages[themeOption as keyof typeof messages]}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
