ARG TZ=America/Lima
ARG NODE_ENV=production

FROM oven/bun:1-slim AS builder

ARG TZ
ARG NODE_ENV

ENV TZ=$TZ
ENV NODE_ENV=$NODE_ENV

# Workspace packages
WORKDIR /app

# Typescript
COPY tsconfig.json ./

# Install dependencies
COPY package.json bun.lock ./

RUN bun install --ci

# Disable Astro telemetry
RUN bun astro telemetry disable

# Build the project
COPY pwa-assets.config.ts ./
COPY tailwind.config.js ./
COPY astro.config.ts ./
COPY public/ public/
COPY src/ src/

RUN bun run build


FROM nginx:1-alpine-slim AS runtime

# Add wget for ready endpoint healthcheck
COPY --from=busybox /lib/* /lib/
COPY --from=busybox /lib64/* /lib64/
COPY --from=busybox /bin/wget /bin/

ARG TZ

ENV TZ=$TZ

# Delete default nginx
RUN apk del -r nginx

RUN apk add --no-cache \
  nginx \
  nginx-mod-http-brotli

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/dist/client/ /usr/share/nginx/html

HEALTHCHECK --interval=120s --timeout=5s --start-period=5s \
  CMD ["/bin/wget", "--spider", "--timeout=5", "http://localhost:80/_health"]
