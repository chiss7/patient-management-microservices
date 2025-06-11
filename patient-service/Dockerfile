FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package

FROM openjdk:17-jdk AS runner

WORKDIR /app

COPY --from=builder ./app/target/*.jar ./app.jar

EXPOSE 4000

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-patient-management:5432/patient-management-db
ENV SPRING_DATASOURCE_USERNAME=chis
ENV SPRING_DATASOURCE_PASSWORD=chis
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SPRING_SQL_INIT_MODE=always

ENTRYPOINT ["java", "-jar", \
  "-Dspring.datasource.url=${SPRING_DATASOURCE_URL}", \
  "-Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME}", \
  "-Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD}", \
  "-Dspring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO}", \
  "-Dspring.sql.init.mode=${SPRING_SQL_INIT_MODE}", \
  "app.jar"]