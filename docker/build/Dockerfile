FROM maven:3.6.3-jdk-11

WORKDIR /usr/src/build

COPY [".", "./"]

CMD ["sh", "-c", "mvn clean verify"]