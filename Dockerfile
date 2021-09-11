FROM openjdk:17-ea-11-jdk-slim
EXPOSE 8081
VOLUME /tmp
COPY build/libs/woomool-market-1.0.jar woomool-market.jar
ENTRYPOINT ["java", "-jar", "woomool-market.jar"]