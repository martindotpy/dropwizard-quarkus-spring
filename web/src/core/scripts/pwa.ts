import { registerSW } from "virtual:pwa-register"

// Listeners
window.addEventListener("load", () => registerSW({ immediate: true }))
window.addEventListener("beforeinstallprompt", (e) => e.preventDefault())
