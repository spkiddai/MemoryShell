#!/bin/bash

catalina.sh start

# 在后台启动Tomcat，并且立即开始尾随日志，但是不阻塞脚本继续执行
tail -f $CATALINA_HOME/logs/catalina.out &
TAIL_PID=$!

# 检查Tomcat端口是否开放（默认8080），最多等待60秒
for i in {1..60}; do
    if nc -z localhost 8080; then
        echo "Tomcat started successfully."
        break
    fi
    echo "Waiting for Tomcat to start..."
    sleep 1
done

# 执行JPS查询PID，假设PID为Tomcat的PID
PID=$(jps | grep Bootstrap | awk '{print $1}')
echo "Tomcat PID: $PID"

# 如果有PID，则执行Sandbox注入
if [ ! -z "$PID" ]; then
  # 将输出重定向到文件，并执行Sandbox注入
  cd /app/sandbox/bin && ./sandbox.sh -p $PID &> /app/sandbox_injection.log
  # 检查上一个命令的退出状态
  if [ $? -eq 0 ]; then
    echo "Sandbox injection succeeded."
  else
    echo "Sandbox injection failed."
  fi
else
  echo "No Tomcat PID found. Sandbox injection skipped."
fi

# 如果需要此脚本结束后继续保持tail运行，可以考虑等待tail进程
wait $TAIL_PID