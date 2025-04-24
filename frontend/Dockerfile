# Etapa 1: build da aplicação Angular
FROM node:18-alpine as builder

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npx ng build helpdeskWeb --configuration production

# Etapa 2: servir a aplicação com NGINX
FROM nginx:alpine

# Remove o default.conf padrão do nginx
RUN rm -rf /etc/nginx/conf.d/default.conf

# Copia o build Angular para a pasta pública do nginx
COPY --from=builder /app/dist/helpdesk-web/browser /usr/share/nginx/html

# Copia a config personalizada do nginx (você pode criar isso)
COPY nginx.conf /etc/nginx/conf.d

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
