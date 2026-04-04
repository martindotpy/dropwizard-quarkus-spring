import { $ } from "@/core/lib/dom-selector"
import lightFavicon from "@assets/svg/light-favicon.svg?url"

// Update favicon
function updateFavicon() {
  // Element
  const favicon = $<HTMLLinkElement>("link[rel~='icon'][type='image/svg+xml']")

  if (!favicon) return

  // Check if user prefers dark mode
  const isDarkPrefers = window.matchMedia(
    "(prefers-color-scheme: dark)"
  ).matches

  if (!isDarkPrefers) return

  // Update favicon
  favicon.href = lightFavicon
}

// Initialize
updateFavicon()
