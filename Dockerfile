FROM maven:3.9.6-amazoncorretto-8 AS builder

RUN mkdir /build

ADD ./MemoryAgent /build/MemoryAgent
ADD ./MemoryRASP /build/MemoryRASP
ADD ./MemoryServer /build/MemoryServer
#ADD ./MemorySpring /build/MemorySpring

RUN cd /build/MemoryAgent && mvn clean package
RUN cd /build/MemoryRASP && mvn clean package
RUN cd /build/MemoryServer && mvn clean package
#RUN cd /build/MemorySpring && maven clean package

# 使用带有OpenJDK 8的基础镜像
FROM openjdk:8-jdk

# 设置工作目录为
WORKDIR /app

# 安装工具
RUN apt-get update && apt-get install -y wget tar unzip netcat

# 下载并解压Tomcat
RUN wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.85/bin/apache-tomcat-9.0.85.tar.gz \
    && tar -xvf apache-tomcat-9.0.85.tar.gz \
    && mv apache-tomcat-9.0.85 /app/tomcat \
    && rm apache-tomcat-9.0.85.tar.gz

# 设置Tomcat环境变量
ENV CATALINA_HOME=/app/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH

# 清理Tomcat的默认应用
RUN rm -rf $CATALINA_HOME/webapps/*

# 下载并解压JVM-Sandbox
RUN wget https://ompc.oss-cn-hangzhou.aliyuncs.com/jvm-sandbox/release/sandbox-1.4.0-bin.zip -O jvm-sandbox.zip \
    && unzip jvm-sandbox.zip -d /app \
    && rm jvm-sandbox.zip

# 创建Token文件
RUN touch /root/.sandbox.token

COPY --from=builder /build/MemoryServer/target/MemoryServer-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/ROOT.war
COPY --from=builder /build/MemoryAgent/target/MemoryAgent-1.0-SNAPSHOT.jar MemoryAgent.jar
#COPY --from=builder /build/MemorySpring/target/MemorySpring-1.0-SNAPSHOT.jar MemorySpring.jar
COPY --from=builder /build/MemoryRASP/target/MemoryRASP-1.0-SNAPSHOT-jar-with-dependencies.jar sandbox/sandbox-module/rasp.jar

# 解决tools.jar加载问题
#ENV CATALINA_OPTS="-Xbootclasspath/a:$JAVA_HOME/lib/tools.jar"
ENV CATALINA_OPTS="-Dcom.sun.management.jmxremote \
                   -Dcom.sun.management.jmxremote.port=12345 \
                   -Dcom.sun.management.jmxremote.rmi.port=12345 \
                   -Dcom.sun.management.jmxremote.authenticate=false \
                   -Dcom.sun.management.jmxremote.ssl=false \
                   -Xbootclasspath/a:$JAVA_HOME/lib/tools.jar"

# 暴露Tomcat端口
EXPOSE 8080

# 暴露JMX端口
EXPOSE 12345

# 复制启动脚本
COPY startup.sh /app/startup.sh

# 设置脚本执行权限
RUN chmod +x /app/startup.sh

# 启动服务
CMD ["/app/startup.sh"]
