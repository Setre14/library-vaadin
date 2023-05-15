FROM eclipse-temurin:17-jdk as builder
COPY . /library/
WORKDIR /library
RUN ./mvnw clean package -Pproduction 

FROM eclipse-temurin:17-jre
COPY --from=builder /library/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
