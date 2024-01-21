FROM maven:3.9.6-eclipse-temurin-21-jammy AS compiler
RUN mkdir /compile
WORKDIR /compile
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /
ENV PORT 8083
COPY --from=compiler /compile/target/*.jar .
CMD ["sh", "-c", "java -jar $(ls *.jar)"]
