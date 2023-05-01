FROM openjdk:11
ENV JAVA_OPTS=-XX:+UseContainerSupport 
ADD InvoiceApplication.jar InvoiceApplication.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "InvoiceApplication.jar"]
