# Stage 1: Build the frontend with Node 20
FROM node:20-alpine as build

WORKDIR /app

COPY package*.json ./
RUN npm install

# Accept REACT_APP_API_BASE_URL build argument
ARG REACT_APP_API_BASE_URL
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL

COPY . .
RUN npm run build

# Stage 2: Serve static files with Nginx
FROM nginx:alpine

COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
