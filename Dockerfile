FROM openjdk:22-jdk
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} ./app.jar

EXPOSE 3000

CMD ["java", "-server", "-XX:+UseCompressedOops", "-XX:+OptimizeStringConcat", "-XX:+UseThreadPriorities", "-XX:+OptimizeFill", "-Dfile.encoding=UTF-8", "-Djava.awt.headless=true", "-jar", "/app/app.jar"]


