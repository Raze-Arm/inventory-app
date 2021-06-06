FROM amazoncorretto:11-alpine-jdk
#RUN mkdir  -p spring-app/files/images/user-photos/placeholder/
RUN mkdir  -p spring-app/
WORKDIR /spring-app

COPY target/inventory-0.0.1-SNAPSHOT.jar /spring-app/inventory-app.jar
RUN mkdir -p files/images/user-photos/placeholder/
COPY /files/images/user-photos/placeholder /spring-app/files/images/user-photos/placeholder/profile-placeholder.jpg

#ARG ALLOWED_ORIGINS
#ARG SPRING_PROFILES_ACTIVE
#ARG JWT_TOKEN
#ARG JWT_TOKEN_EXPIRE
#ARG JWT_TOKEN_PREFIX
#ARG MYSQL_USERNAME
#ARG MYSQL_PASSWORD
#ARG MYSQL_HOST
#ARG MYSQL_DB_NAME
#
#ENV ALLOWED_ORIGINS ${ALLOWED_ORIGINS}
#ENV SPRING_PROFILES_ACTIVE ${SPRING_PROFILES_ACTIVE}
#ENV JWT_TOKEN ${JWT_TOKEN}
#ENV JWT_TOKEN_EXPIRE ${JWT_TOKEN_EXPIRE}
#ENV JWT_TOKEN_PREFIX ${JWT_TOKEN_PREFIX}
#ENV MYSQL_USERNAME ${MYSQL_USERNAME}
#ENV MYSQL_PASSWORD ${MYSQL_PASSWORD}
#ENV MYSQL_HOST ${MYSQL_HOST}
#ENV MYSQL_DB_NAME ${MYSQL_DB_NAME}




ENTRYPOINT ["java", "-jar" , "/spring-app/inventory-app.jar"]