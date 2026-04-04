import node from "@astrojs/node"
import react from "@astrojs/react"
import tailwindcss from "@tailwindcss/vite"
import { devtools } from "@tanstack/devtools-vite"
import { tanstackRouter } from "@tanstack/router-plugin/vite"
import astroPwa from "@vite-pwa/astro"
import compress from "astro-compress"
import compressor from "astro-compressor"
import { defineConfig, fontProviders } from "astro/config"
import checker from "vite-plugin-checker"
import svgr from "vite-plugin-svgr"
import { appName, appShortName } from "./src/core/constant/seo-constant"

// Context
const site =
  process.env.COOLIFY_URL || "https://dropwizard-quarkus-spring.martindotpy.dev"
const { DEV: isDev } = import.meta.env

// Config
export default defineConfig({
  site,
  trailingSlash: "never",

  integrations: [
    react({
      babel: {
        plugins: ["babel-plugin-react-compiler"],
      },
    }),
    astroPwa({
      includeAssets: ["favicon.svg"],
      registerType: "autoUpdate",
      pwaAssets: {
        config: true,
      },
      manifest: {
        lang: "es",
        name: appName,
        short_name: appShortName,
        theme_color: "#002263",
        background_color: "#090b0c",
        display: "standalone",
      },
      workbox: {
        navigateFallback: "/_shell",
        navigateFallbackDenylist: [/^\/api/],
        maximumFileSizeToCacheInBytes: 3 * 1024 * 1024, // 3MB
        globPatterns: [
          "**/*.{html,png,jpg,jpeg,svg,webp,avif,gif,ico,js,css,woff2,woff,ttf,otf}",
        ],
        globIgnores: [
          "**\\/node_modules\\/**\\/*",
          "\\/src\\/**\\/*",
          "**/index.png",
          "index.png",
          "sw.js",
          "workbox-*.js",
        ],
      },
      devOptions: {
        enabled: false,
        navigateFallbackAllowlist: [],
      },
    }),
    compress({
      HTML: {
        "html-minifier-terser": false,
      },
      Exclude: "favicon.svg",
    }),
    compressor({ zstd: false }),
  ],

  vite: {
    plugins: [
      devtools(),
      tanstackRouter({
        target: "react",
        autoCodeSplitting: true,
        routesDirectory: "src/pages/_app/routes",
        generatedRouteTree: "src/pages/_app/routeTree.gen.ts",
        routeFileIgnorePrefix: "-",
        quoteStyle: "double",
      }),
      svgr(),
      ...(isDev
        ? [
            checker({
              typescript: true,
            }),
          ]
        : []),
      tailwindcss(),
    ],
    envPrefix: "PUBLIC_",
    server: {
      proxy: {
        "/api/dropwizard": "http://localhost:8080",
        "/api/quarkus": "http://localhost:8081",
        "/api/spring": "http://localhost:8082",
      },
    },
  },

  experimental: {
    fonts: [
      {
        provider: fontProviders.google(),
        name: "Geist",
        cssVariable: "--font-geist",
        subsets: ["latin"],
        weights: ["100 900"],
        styles: ["normal"],
      },
      {
        provider: fontProviders.google(),
        name: "JetBrains Mono",
        cssVariable: "--font-jetbrains-mono",
        subsets: ["latin"],
        weights: ["100 800"],
        styles: ["normal"],
        fallbacks: ["monospace"],
      },
    ],
    contentIntellisense: true,
  },

  image: { layout: "constrained" },

  // Not necessary because pre-caching of the workbox was configured
  prefetch: false,

  devToolbar: {
    enabled: false,
  },

  adapter: node({ mode: "standalone" }),
})
