FROM openjdk:17 AS build-env
COPY ./ ./
RUN microdnf install findutils
RUN ./gradlew bootJar

#az keyvault secret download --name chat-application --vault-name app-env -f
FROM openjdk:17
WORKDIR /App
COPY --from=build-env build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]