FROM java:8
EXPOSE 8080
ENV TZ=UTC
ADD ./target/desglose-1.0.0.war /desglose.war
ENTRYPOINT ["java","-jar", "desglose.war"]

