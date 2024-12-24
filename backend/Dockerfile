FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY backend/ /app/
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/furniture-store.jar furniture-store.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "furniture-store.jar"]
