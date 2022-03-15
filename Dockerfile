FROM maven:3.6.3-jdk-11 as builder
WORKDIR /source
COPY . /source
RUN mvn clean package

FROM maven:3.6.3-jdk-11
WORKDIR /app
COPY --from=builder /source/target/accounting.jar /app

EXPOSE 8000
CMD java -jar accounting.jar