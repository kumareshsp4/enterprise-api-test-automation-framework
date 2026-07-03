FROM maven:3.9-eclipse-temurin-17

WORKDIR /workspace

COPY pom.xml .

RUN mvn --batch-mode -DskipTests dependency:go-offline

COPY src ./src

ENTRYPOINT ["mvn", "--batch-mode"]

CMD ["clean", "test"]