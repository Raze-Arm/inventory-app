FROM amazoncorretto:11-alpine-jdk
COPY target/inventory-0.0.1-SNAPSHOT.jar inventory-app.jar



ENTRYPOINT ["java", "-jar" , "/inventory-app.jar"]