FROM eclipse-temurin:17-jre
COPY build/libs/blog.jar blog.jar
ENTRYPOINT ["java", "-jar", "/blog.jar"]
