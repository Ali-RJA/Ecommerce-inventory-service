# Use Maven and Java 17 base image to build the application
FROM maven:3.8.3-openjdk-17 AS build

# Copy your source code and pom.xml file into the container
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

# Build the application in the container
RUN mvn -f /usr/src/app/pom.xml clean package

# Use Java 17 runtime base image
FROM openjdk:17-jdk-slim

# Copy the built application from the build stage to the runtime image
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar

# Specify the entry point of the application
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]

