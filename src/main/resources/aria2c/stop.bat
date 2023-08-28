@echo off
set /a port = 6800
for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do ( set pid=%%i)
echo '查找到的进程号为%pid%'
echo '开始强制杀死进程'

taskkill /f /pid   %pid%    /t

echo '进程杀死完毕'

