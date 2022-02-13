FROM maven:3.8.4-jdk-11 as build-stage

WORKDIR /function

COPY pom.xml /function/pom.xml

ARG namespace
ARG bucket_name
ARG db_user
ARG db_password
ARG db_url

ENV NAMESPACE=${namespace}
ENV BUCKET_NAME=${bucket_name}
ENV DB_USER=${db_user}
ENV DB_PASSWORD=${db_password}
ENV DB_URL=${db_url}

RUN ["mvn", "install"]

# Copies build source into build container
COPY src /function/src
RUN ["mvn", "package"]

FROM fnproject/fn-java-fdk:jre11-1.0.145
WORKDIR /function
COPY --from=build-stage /function/target/*.jar /function/app
CMD ["me.shukawam.fn.petstore.PetstoreDatabase::handleRequest"]