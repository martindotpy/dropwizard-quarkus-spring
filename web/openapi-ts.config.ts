import { defineConfig } from "@hey-api/openapi-ts"

export default defineConfig({
  input: [
    "http://localhost:8080/api/dropwizard/openapi.json",
    "http://localhost:8081/api/quarkus/openapi.json",
    "http://localhost:8082/api/spring/openapi.json",
  ],
  output: {
    path: "src/api/client",
    postProcess: ["prettier"],
  },
  watch: true,

  plugins: [
    {
      name: "@hey-api/client-fetch",
      baseUrl: false,
    },
    "@tanstack/react-query",
    "zod",
  ],
})
