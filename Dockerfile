FROM amazoncorretto:11-alpine-jdk
#FROM maven:3.8.1-jdk-11

#RUN mkdir  -p spring-app/files/images/user-photos/placeholder/
RUN mkdir   -p spring-app/files/images
WORKDIR /spring-app

COPY target/inventory-0.0.1-SNAPSHOT.jar /spring-app/inventory-app.jar
#RUN mkdir -p files/images/user-photos/placeholder/
#COPY /files/images/user-photos/placeholder /spring-app/files/images/user-photos/placeholder/profile-placeholder.jpg
COPY profile-placeholder.jpg /spring-app

#RUN mvn clean install





ENTRYPOINT ["java", "-jar" , "/spring-app/inventory-app.jar"]
#ENTRYPOINT ["java", "-jar" , "/spring-app/target/inventory-0.0.1-SNAPSHOT.jar"]