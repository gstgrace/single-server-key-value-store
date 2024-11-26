FROM bellsoft/liberica-openjdk-alpine-musl:11 AS client-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN mkdir -p out
RUN javac -d out \
    src/main/java/common/*.java \
    src/main/java/server/*.java \
    src/main/java/client/*.java

FROM bellsoft/liberica-openjdk-alpine-musl:11 AS server-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac -d out \
    src/main/java/common/*.java \
    src/main/java/server/*.java \
    src/main/java/client/*.java
# cmd to run server locally - logic is handled in the deploy.sh so the protocal can be passed in dynamically
CMD ["java", "-cp", "out", "server.ServerApp"]


