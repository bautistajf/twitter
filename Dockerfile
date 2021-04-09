FROM openjdk:8
VOLUME /tmp
EXPOSE 8081
EXPOSE 8082
ADD ./build/libs/twitter-1.0.0.jar twitter-srv.jar
ENTRYPOINT ["java","-jar","/twitter-srv.jar"]