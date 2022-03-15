FROM maven:3.6.3-jdk-11 as builder
WORKDIR /source
COPY . /source

# сlean для очистки папки target, package - для упаковки скомпилированных файлов в .jar
RUN mvn clean package

FROM maven:3.6.3-jdk-11
WORKDIR /app
COPY --from=builder /source/target/accounting.jar /app

EXPOSE 8000
# запуск сервера
CMD java -jar accounting.jar