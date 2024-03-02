FROM openjdk:17-jdk-slim
WORKDIR /app
COPY ./target/hs2-actors-0.0.1-SNAPSHOT.jar ./hs-actors.jar
ENTRYPOINT ["java","-jar","/app/hs-actors.jar"]