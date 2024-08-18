FROM node:20 as build
WORKDIR /app
COPY ./my-budget-frontend/package*.json ./
RUN npm install
COPY ./my-budget-frontend ./
RUN npm run build --prod

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY ./Deployment/nginx-custom.conf /etc/nginx/conf.d/default.conf
EXPOSE 80