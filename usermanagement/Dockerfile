FROM maven:3.9.6-eclipse-temurin-21-jammy AS compiler
RUN mkdir /compile
WORKDIR /compile
ENV PORT 8080
ENV HOST 0.0.0.0
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /
COPY --from=compiler /compile/target/*.jar .
EXPOSE 8080
CMD ["sh", "-c", "java -jar $(ls *.jar)"]