import { defineConfig } from "@hey-api/openapi-ts"

export default defineConfig({
  input: [
    {
      path: "http://localhost:8080/api/dropwizard/openapi.json",
      watch: true,
    },
    {
      path: "http://localhost:8081/api/quarkus/openapi.json",
      watch: true,
    },
    {
      path: "http://localhost:8082/api/spring/openapi.json",
      watch: true,
    },
  ],
  output: {
    path: "src/api/client",
    postProcess: ["prettier"],
  },

  plugins: [
    {
      name: "@hey-api/client-fetch",
      baseUrl: false,
    },
    "@tanstack/react-query",
    "zod",
  ],
})
