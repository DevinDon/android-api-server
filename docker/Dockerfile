FROM openjdk:11
WORKDIR /app
RUN curl -LJo app.jar https://github.com/DevinDon/android-api-server/releases/download/0.1.0/android-0.1.0.jar
CMD ["java", "-jar", "/app/app.jar"]
