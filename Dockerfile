# Create image with name persons-interface-image
# For creating image run below command
    # docker build -t persons-interface-image .
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
