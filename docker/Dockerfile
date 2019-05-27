FROM registry.cn-shanghai.aliyuncs.com/iinfinity/openjdk
WORKDIR /app
RUN git clone https://github.com/DevinDon/android-api-server.git
RUN cd android-api-server && mvn package -Dmaven.test.skip=true
RUN mv /app/android-api-server/target/android*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
